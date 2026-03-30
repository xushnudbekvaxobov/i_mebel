package imebel.imebel.dto.response;

import imebel.imebel.util.enums.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductResponseDto {
    private UUID id;
    private String SpecializationName;
    private String name;
    private String description;
    private BigDecimal  price;
    private String material;
    private String color;
    private Double length;
    private Double width;
    private Double height;
    private ProductStatus status;
    List<ProductImageResponseDto> imageList;
}
