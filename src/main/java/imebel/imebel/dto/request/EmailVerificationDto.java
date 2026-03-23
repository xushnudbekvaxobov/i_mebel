package imebel.imebel.dto.request;

import lombok.Data;

@Data
public class EmailVerificationDto {
    private Long userId;
    private String verificationCode;
}
