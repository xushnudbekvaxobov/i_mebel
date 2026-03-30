package imebel.imebel.service;

import imebel.imebel.entity.UserEntity;

public interface EmailService {
     String generateVerificationCode();
     void sendVerificationCodeForRegister(UserEntity userEntity, String verificationCode);
     void sendVerificationCodeForReset(UserEntity userEntity, String verificationCode);
}
