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
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailVerificationMapper emailVerificationMapper;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private final Integer codeExpiration = 2;
    private final Integer blockMinutes = 15;
    private final JavaMailSender mailSender;



    @Override
    @Transactional
    public void sendVerificationCode(String toEmail, String verificationCode) {
        UserEntity userEntity = userRepository.findByEmail(toEmail).orElseThrow(() -> new DataNotFoundException("user not found with email: " + toEmail));
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findByUserEntity_Email(toEmail)
                .orElseGet(() -> emailVerificationRepository.save(emailVerificationMapper.toEntity(userEntity, verificationCode, codeExpiration)));
        Integer attemptCount = emailVerificationEntity.getAttemptCount();
        if (emailVerificationEntity.getBlockedUntil() != null && emailVerificationEntity.getBlockedUntil().isAfter(LocalDateTime.now())) {
            emailVerificationEntity.setAttemptCount(0);
            emailVerificationRepository.save(emailVerificationEntity);
            throw new AppBadException("Your account is blocked until " + emailVerificationEntity.getBlockedUntil());
        }
        if (attemptCount.equals(5)) {
            emailVerificationEntity.setBlockedUntil(LocalDateTime.now().plusMinutes(blockMinutes));
            emailVerificationRepository.save(emailVerificationEntity);
            throw new AppBadException("Please, try " + blockMinutes + " minutes later");
        }
        emailVerificationEntity.setVerificationCode(verificationCode);
        emailVerificationEntity.setAttemptCount(++attemptCount);
        emailVerificationRepository.save(emailVerificationEntity);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject("iMebel Verification Code");
            mimeMessageHelper.setText(buildEmailTemplate(verificationCode),true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MailException | MessagingException e) {
            throw new MailSendException("Failed to send verification code: " + e);
        }
    }
    public String generateVerificationCode() {
        int code = new SecureRandom().nextInt(10000, 99999);
        return String.valueOf(code);
    }


    private String buildEmailTemplate(String code) {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>iMebel Verification Code</title>
        </head>
        <body style="margin:0; padding:0; background-color:#f4f4f7; font-family:Arial, sans-serif;">
        <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background-color:#f4f4f7; padding:40px 0;">
            <tr>
                <td align="center">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:600px; background:#ffffff; border-radius:16px; overflow:hidden; box-shadow:0 4px 14px rgba(0,0,0,0.08);">
                        <tr>
                            <td style="background:#111827; padding:24px; text-align:center;">
                                <h1 style="margin:0; color:#ffffff; font-size:28px;">iMebel</h1>
                                <p style="margin:8px 0 0; color:#d1d5db; font-size:14px;">Furniture services platform</p>
                            </td>
                        </tr>

                        <tr>
                            <td style="padding:40px 32px;">
                                <h2 style="margin:0 0 16px; font-size:24px; color:#111827;">Email verification</h2>
                                <p style="margin:0 0 16px; font-size:16px; color:#4b5563; line-height:1.6;">
                                    Hello,
                                </p>
                                <p style="margin:0 0 24px; font-size:16px; color:#4b5563; line-height:1.6;">
                                    Thank you for registering in <strong>iMebel</strong>. Please use the verification code below to complete your registration.
                                </p>

                                <div style="text-align:center; margin:32px 0;">
                                    <div style="display:inline-block; background:#f3f4f6; border:2px dashed #d1d5db; border-radius:12px; padding:18px 32px; font-size:32px; font-weight:bold; letter-spacing:8px; color:#111827;">
                                        %s
                                    </div>
                                </div>

                                <p style="margin:0 0 16px; font-size:15px; color:#6b7280; line-height:1.6; text-align:center;">
                                    This code will expire in <strong>2 minutes</strong>.
                                </p>

                                <p style="margin:24px 0 0; font-size:14px; color:#9ca3af; line-height:1.6;">
                                    If you did not request this email, you can safely ignore it.
                                </p>
                            </td>
                        </tr>

                        <tr>
                            <td style="background:#f9fafb; padding:20px 32px; text-align:center; border-top:1px solid #e5e7eb;">
                                <p style="margin:0; font-size:13px; color:#9ca3af;">
                                    © 2026 iMebel. All rights reserved.
                                </p>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        </body>
        </html>
        """.formatted(code);
    }
}
