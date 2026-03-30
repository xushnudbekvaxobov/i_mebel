package imebel.imebel.repository;

import imebel.imebel.entity.EmailVerificationEntity;
import imebel.imebel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, UUID> {
    Optional<EmailVerificationEntity> findByUserEntity_Email(String userEntityEmail);
    Optional<EmailVerificationEntity> findByUserEntity(UserEntity userEntity);

    boolean existsByUserEntity_EmailAndVerificationCode(String userEntityEmail, String verificationCode);
}
