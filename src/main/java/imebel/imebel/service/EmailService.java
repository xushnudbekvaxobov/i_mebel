package imebel.imebel.service;

import imebel.imebel.entity.UserEntity;

public interface EmailService {
    void sendVerificationCode(String toEmail, String verificationCode);
     String generateVerificationCode();
}
