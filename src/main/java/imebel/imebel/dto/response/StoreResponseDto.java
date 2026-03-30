package imebel.imebel.dto.response;

import imebel.imebel.util.enums.ApprovalStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StoreResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String bannerImageUrl;
    private String bannerImageId;
    private ApprovalStatus  approvalStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
