package imebel.imebel.service;

import imebel.imebel.dto.request.LoginDto;
import imebel.imebel.dto.request.ResetPasswordDto;
import imebel.imebel.dto.request.UserDto;

import imebel.imebel.dto.request.UserUpdateDto;
import imebel.imebel.dto.response.LoginResponseDto;
import imebel.imebel.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void register(UserDto userDto);
    LoginResponseDto login(LoginDto loginDto);
    void verifyEmail(String email, String verificationCode);
    void sendVerificationCodeForForgotPassword(String email);
    void resetPassword(String email, ResetPasswordDto resetPasswordDto);
    UserResponseDto getCurrentUser(String email);
    UserResponseDto updateCurrentUser(UserUpdateDto  userUpdateDto, String email);
    void deleteMyAccount(String email);

}
