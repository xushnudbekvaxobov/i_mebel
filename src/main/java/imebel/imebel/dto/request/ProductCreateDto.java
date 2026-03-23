package imebel.imebel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreateDto {
    @NotBlank(message = "name is can't be empty")
    private String name;
    @NotBlank(message = "specializationName is can't be empty")
    private String specializationName;
    @NotBlank(message = "description is can't be empty")
    private String description;
    @NotNull(message = "price is can't be empty")
    private BigDecimal price;
    @NotBlank(message = "material is can't be empty")
    private String material;
    @NotBlank(message = "color is can't be empty")
    private String color;
    @NotNull(message = "length is can't be empty")
    private Double length;
    @NotNull(message = "width is can't be empty")
    private Double width;
    @NotNull(message = "height is can't be empty")
    private Double height;
}
