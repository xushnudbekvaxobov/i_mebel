package imebel.imebel.mapper;

import imebel.imebel.entity.EmailVerificationEntity;
import imebel.imebel.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailVerificationMapper {
    @Value("${email.sending.code.expiration.minutes}")
    private Integer expirationMinutes;
    public EmailVerificationEntity toEntity(UserEntity userEntity, String verificationCode){
        return EmailVerificationEntity.builder()
                .userEntity(userEntity)
                .verificationCode(verificationCode)
                .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .attemptCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
