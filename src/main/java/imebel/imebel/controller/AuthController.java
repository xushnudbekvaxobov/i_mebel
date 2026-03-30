package imebel.imebel.controller;

import imebel.imebel.dto.request.LoginDto;
import imebel.imebel.dto.request.ResetPasswordDto;
import imebel.imebel.dto.request.UserDto;
import imebel.imebel.dto.response.ApiResponse;
import imebel.imebel.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserDto userDto){
        userService.register(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"successfully",null,200));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"successfully", userService.login(loginDto),200));
    }

    @PatchMapping("verify-email/{verificationCode}")
    public ResponseEntity<ApiResponse<Void>> verifyEmail( @PathVariable String verificationCode, @RequestParam String email){
        userService.verifyEmail(email, verificationCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(true,"successfully",null,200));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email){
        userService.sendVerificationCodeForForgotPassword(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new  ApiResponse<>(true,"successfully",null,200));
    }


    @PatchMapping("/reset-password/{email}")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable String email, @Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        System.out.println("email: " + email);
        userService.resetPassword(email, resetPasswordDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true,"password reset",null,200));
    }

}
