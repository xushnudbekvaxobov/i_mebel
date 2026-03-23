package imebel.imebel.mapper;

import imebel.imebel.entity.EmailVerificationEntity;
import imebel.imebel.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailVerificationMapper {
    public EmailVerificationEntity toEntity(UserEntity userEntity, String verificationCode, Integer codeExpiration){
        return EmailVerificationEntity.builder()
                .userEntity(userEntity)
                .verificationCode(verificationCode)
                .expiresAt(LocalDateTime.now().plusMinutes(codeExpiration))
                .attemptCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
