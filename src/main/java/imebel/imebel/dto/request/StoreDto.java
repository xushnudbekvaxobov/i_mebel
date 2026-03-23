package imebel.imebel.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StoreDto {
    private String name;
    private String description;
    private String telegramLink;
    private String instagramLink;
    private String address;
    private BigDecimal  latitude;
    private BigDecimal longitude;
}
