package imebel.imebel.repository;

import imebel.imebel.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity,Long> {
    Optional<EmailVerificationEntity> findByUserEntity_Email(String userEntityEmail);

    boolean existsByUserEntity_EmailAndVerificationCode(String userEntityEmail, String verificationCode);
}
