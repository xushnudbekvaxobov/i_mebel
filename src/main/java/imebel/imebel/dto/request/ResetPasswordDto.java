package imebel.imebel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank(message = "Verification code is required")
    private String verificationCode;
    @NotBlank(message = "new password required")
    @Size(min = 8, max = 10, message = "password must be between 8 and 10 characters")
    private String newPassword;
    @NotBlank(message = "confirm password required")
    @Size(min = 8, max = 10, message = "password must be between 8 and 10 characters")
    private String confirmPassword;
}
