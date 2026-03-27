package imebel.imebel.service.serviceImpl;

import imebel.imebel.dto.request.LoginDto;
import imebel.imebel.dto.request.ResetPasswordDto;
import imebel.imebel.dto.request.UserDto;
import imebel.imebel.dto.request.UserUpdateDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.dto.response.LoginResponseDto;
import imebel.imebel.dto.response.UserResponseDto;
import imebel.imebel.entity.EmailVerificationEntity;
import imebel.imebel.entity.UserEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.jwt.JwtService;
import imebel.imebel.mapper.EmailVerificationMapper;
import imebel.imebel.mapper.UserMapper;
import imebel.imebel.repository.EmailVerificationRepository;
import imebel.imebel.repository.UserRepository;
import imebel.imebel.service.EmailService;
import imebel.imebel.service.UserService;
import imebel.imebel.util.enums.UserRole;
import imebel.imebel.util.enums.UserStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationMapper emailVerificationMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, EmailVerificationRepository emailVerificationRepository, EmailVerificationMapper emailVerificationMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.emailVerificationMapper = emailVerificationMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    @Override
    @Transactional
    public void register(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()){
            throw new AppBadException("user already exist");
        }
        String verificationCode = emailService.generateVerificationCode();
        userRepository.save(userMapper.toEntity(userDto));
        emailService.sendVerificationCode(userDto.getEmail(),verificationCode);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        UserEntity userEntity = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new DataNotFoundException("User not found with email: " + loginDto.getEmail()));
        if(!bCryptPasswordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())){
            throw new AppBadException("Your password is invalid password");
        }
        System.out.println("role" + userEntity.getRole());
        return new LoginResponseDto(jwtService.generateToken(userEntity));
    }

    @Override
    public void verifyEmail(String email, String verificationCode) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("Verification code not found with email: " + email));
       if (!emailVerificationEntity.getExpiresAt().isBefore(LocalDateTime.now().plusMinutes(2))){
            throw new AppBadException("Verification code expired");
       }
        if (!emailVerificationEntity.getVerificationCode().equals(verificationCode)) {
            throw new AppBadException("Verification code does not match");
        }
        userEntity.setStatus(UserStatus.ACTIVE);
        userRepository.save(userEntity);
    }

    @Override
    public void sendVerificationCodeForForgotPassword(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
        String verificationCode = emailService.generateVerificationCode();
        emailVerificationMapper.toEntity(userEntity, verificationCode, 2);
        emailService.sendVerificationCode(email, verificationCode);
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        UserEntity userEntity = userRepository.findByEmail(resetPasswordDto.getEmail()).orElseThrow(() -> new DataNotFoundException("User not found with email: " + resetPasswordDto.getEmail()));
        if(!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
            throw new AppBadException("Passwords do not match");
        }
        if(userEntity.getStatus().equals(UserStatus.NOT_ACTIVE) || userEntity.getStatus().equals(UserStatus.DELETED)){
            throw new AppBadException("Email NOT_ACTIVE or DELETED");
        }
        EmailVerificationEntity emailVerification = userEntity.getEmailVerificationEntity();
        if(emailVerification == null || !emailVerification.getVerificationCode().equals(resetPasswordDto.getVerificationCode()) || emailVerification.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new AppBadException("Verification code is don't match");
        }
        if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppBadException("Verification code expired");
        }
        userEntity.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public UserResponseDto getCurrentUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
        return userMapper.toResponseDto(userEntity);
    }

    @Override
    public UserResponseDto updateCurrentUser(UserUpdateDto userUpdateDto, String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
        UserEntity userEntity = userRepository.save(userMapper.toEntity(userUpdateDto));
        return userMapper.toResponseDto(userEntity);
    }

    @Override
    public void deleteMyAccount(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("User not found with email: " + email));
        userEntity.setStatus(UserStatus.DELETED);
        userRepository.save(userEntity);
    }

}
