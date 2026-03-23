package imebel.imebel.dto.request;

import imebel.imebel.util.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String address;
    private UserRole role;
}
