package imebel.imebel.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class ProductImageResponseDto {
    private UUID id;
    private String url;
    private boolean isMain;
}
