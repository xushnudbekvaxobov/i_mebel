package imebel.imebel.mapper;

import imebel.imebel.dto.request.UserDto;
import imebel.imebel.dto.request.UserUpdateDto;
import imebel.imebel.dto.response.UserResponseDto;
import imebel.imebel.entity.UserEntity;
import imebel.imebel.util.enums.AuthProvider;
import imebel.imebel.util.enums.UserRole;
import imebel.imebel.util.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity toEntity(UserDto userDto) {
        return UserEntity.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .authProvider(AuthProvider.LOCAL)
                .providerId(null)
                .address(userDto.getAddress())
                .role(userDto.getRole())
                .status(UserStatus.NOT_ACTIVE)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }
    public UserEntity toEntity(UserEntity userEntity, UserDto userDto) {
            userEntity.setFirstName(userDto.getFirstName());
            userEntity.setLastName(userDto.getLastName());
            userEntity.setPhone(userDto.getPhone());
            userEntity.setEmail(userDto.getEmail());
            userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userEntity.setAuthProvider(AuthProvider.LOCAL);
            userEntity.setProviderId(null);
            userEntity.setAddress(userDto.getAddress());
            userEntity.setRole(userDto.getRole());
            userEntity.setStatus(UserStatus.NOT_ACTIVE);
            userEntity.setIsAccountNonExpired(true);
            userEntity.setIsAccountNonLocked(true);
            userEntity.setIsCredentialsNonExpired(true);
            userEntity.setIsEnabled(true);
            return userEntity;
        }
    public UserEntity toEntity(UserEntity userEntity, UserUpdateDto userUpdateDto) {
        userEntity.setFirstName(userUpdateDto.getFirstName());
        userEntity.setLastName(userUpdateDto.getLastName());
        userEntity.setPhone(userUpdateDto.getPhone());
        userEntity.setAddress(userUpdateDto.getAddress());
        return userEntity;
    }

    public UserResponseDto toResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .phone(userEntity.getPhone())
                .email(userEntity.getEmail())
                .address(userEntity.getAddress())
                .role(userEntity.getRole())
                .status(userEntity.getStatus())
                .build();
    }
}
