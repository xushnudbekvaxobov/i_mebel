package imebel.imebel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @Email(message = "email format is not supported")
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 8, max = 10, message = "password must be between 8 and 10 characters")
    private String password;
}
