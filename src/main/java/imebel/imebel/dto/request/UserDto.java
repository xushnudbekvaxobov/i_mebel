package imebel.imebel.dto.request;

import imebel.imebel.util.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "firstName required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private String firstName;
    @NotBlank(message = "firstName required")
    @Size(min = 3, max = 50, message = "firstName must be ")
    private String lastName;
    @Pattern(regexp = "\\d{9}", message = "Phone must be exactly 9 digits")
    private String phone;
    @Email(message = "email format is not supported")
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 8, max = 10, message = "password must be between 8 and 10 characters")
    private String password;
    @NotBlank(message = "Address required")
    private String address;
    @NotNull(message = "Role required")
    private UserRole role;
}
