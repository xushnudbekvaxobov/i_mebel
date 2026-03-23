package imebel.imebel.dto.request;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
