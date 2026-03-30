package imebel.imebel.dto.response;

import imebel.imebel.util.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private CategoryStatus status;
}
