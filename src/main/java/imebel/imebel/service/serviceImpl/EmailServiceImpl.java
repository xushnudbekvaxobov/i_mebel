package imebel.imebel.service.serviceImpl;

import imebel.imebel.entity.EmailVerificationEntity;
import imebel.imebel.entity.UserEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.mapper.EmailVerificationMapper;
import imebel.imebel.repository.EmailVerificationRepository;
import imebel.imebel.repository.UserRepository;
import imebel.imebel.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationMapper emailVerificationMapper;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${email.sending.block.minutes}")
    private  Integer blockMinutes;
    @Value("${email.sending.code.expiration.minutes}")
    private  Integer codeExpiration;
    @Value("${email.sending.attempt.count}")
    private  Integer attemptCount;
    private final JavaMailSender mailSender;



    @Override
    @Transactional
    public void sendVerificationCodeForRegister(UserEntity userEntity, String verificationCode) {
        String toEmail = userEntity.getEmail();
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findByUserEntity(userEntity).orElseThrow(() -> new DataNotFoundException("Email verification code not found from database"));
        Integer attempt = emailVerificationEntity.getAttemptCount();
        if (emailVerificationEntity.getBlockedUntil() != null && emailVerificationEntity.getBlockedUntil().isAfter(LocalDateTime.now())) {
          /*  emailVerificationEntity.setAttemptCount(0);
            emailVerificationRepository.save(emailVerificationEntity); */
            throw new AppBadException("Your account is blocked until " + emailVerificationEntity.getBlockedUntil());
        }
        if (attempt.equals(attemptCount)) {
            emailVerificationEntity.setBlockedUntil(LocalDateTime.now().plusMinutes(blockMinutes));
            emailVerificationRepository.save(emailVerificationEntity);
            throw new AppBadException("Please, try " + blockMinutes + " minutes later");
        }
        emailVerificationEntity.setVerificationCode(verificationCode);
        emailVerificationEntity.setAttemptCount(++attempt);
        emailVerificationRepository.save(emailVerificationEntity);
        sendVerificationCode(toEmail, buildRegisterEmail(verificationCode));
    }

    @Override
    @Transactional
    public void sendVerificationCodeForReset(UserEntity userEntity, String verificationCode) {
        String toEmail = userEntity.getEmail();
        EmailVerificationEntity emailVerificationEntity = userEntity.getEmailVerificationEntity();
        if (emailVerificationEntity.getBlockedUntil() != null && emailVerificationEntity.getBlockedUntil().isAfter(LocalDateTime.now())) {
            if (emailVerificationEntity.getAttemptCount().equals(attemptCount)) {
                emailVerificationEntity.setAttemptCount(0);
                emailVerificationRepository.save(emailVerificationEntity);
            }
            throw new MailSendException("Your account is blocked until " + emailVerificationEntity.getBlockedUntil());
        }
        if (emailVerificationEntity.getAttemptCount().equals(attemptCount)){
            emailVerificationEntity.setBlockedUntil(LocalDateTime.now().plusMinutes(blockMinutes));
        }
        emailVerificationEntity.setVerificationCode(verificationCode);
        emailVerificationRepository.save(emailVerificationEntity);
        sendVerificationCode(toEmail, buildForgotPasswordEmail(verificationCode));
    }

    private void sendVerificationCode(String toEmail, String template) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("iMebel Verification Code");
            mimeMessageHelper.setText(template,true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MailException | MessagingException e) {
            throw new MailSendException("Failed to send verification code: " + e);
        }
    }

    public String generateVerificationCode() {
        int code = new SecureRandom().nextInt(10000, 99999);
        return String.valueOf(code);
    }

    private String loadTemplate(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/" + fileName);
            InputStream inputStream = resource.getInputStream();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AppBadException("Failed to load email template: " + fileName);
        }
    }
    private String buildRegisterEmail(String code) {
        String html = loadTemplate("register.html");
        return html.replace("{{code}}", code);
    }

    private String buildForgotPasswordEmail(String code) {
        String html = loadTemplate("forgot-password.html");
        return html.replace("{{code}}", code);
    }

}
