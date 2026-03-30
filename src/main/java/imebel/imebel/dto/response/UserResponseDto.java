package imebel.imebel.dto.response;


import imebel.imebel.util.enums.UserRole;
import imebel.imebel.util.enums.UserStatus;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private UserRole role;
    private UserStatus status;
}
