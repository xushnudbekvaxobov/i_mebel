package imebel.imebel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank(message = "Email required")
    @Email(message = "Email format is invalid")
    private String email;
    @NotBlank(message = "Verification code is required")
    private String verificationCode;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password might be 8 letter")
    private String newPassword;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password might be 8 letter")
    private String confirmPassword;
}
