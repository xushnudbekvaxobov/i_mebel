package imebel.imebel.dto.request;

import lombok.Data;

@Data
public class ProductImageCreateDto {
    private String imageUrl;
    private String publicId;
    public boolean isMain;
}
