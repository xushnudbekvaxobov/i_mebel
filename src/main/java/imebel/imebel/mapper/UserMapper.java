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

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity toEntity(UserDto userDto) {
        LocalDate now = LocalDate.now();

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
                .createdAt(now)
                .updatedAt(now)
                .status(UserStatus.NOT_ACTIVE)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }
    public UserEntity toEntity(UserUpdateDto userUpdateDto) {
        return UserEntity.builder()
                .firstName(userUpdateDto.getFirstName())
                .lastName(userUpdateDto.getLastName())
                .phone(userUpdateDto.getPhone())
                .address(userUpdateDto.getAddress())
                .build();
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

