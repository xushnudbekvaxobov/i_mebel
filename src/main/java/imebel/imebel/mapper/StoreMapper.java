package imebel.imebel.mapper;

import imebel.imebel.dto.request.StoreDto;
import imebel.imebel.dto.response.StoreResponseDto;
import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.UserEntity;
import imebel.imebel.util.enums.ApprovalStatus;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    public StoreEntity toEntity(StoreDto storeDto, UserEntity userEntity, String imageUrl,  String publicId) {
        return StoreEntity.builder()
                .userEntity(userEntity)
                .name(storeDto.getName())
                .description(storeDto.getDescription())
                .address(storeDto.getAddress())
                .latitude(storeDto.getLatitude())
                .longitude(storeDto.getLongitude())
                .bannerImageUrl(imageUrl)
                .bannerImageId(publicId)
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }
    public StoreEntity toEntity(StoreEntity storeEntity, StoreDto storeDto) {
        storeEntity.setName(storeDto.getName());
        storeEntity.setDescription(storeDto.getDescription());
        storeEntity.setAddress(storeDto.getAddress());
        storeEntity.setLatitude(storeDto.getLatitude());
        storeEntity.setLongitude(storeDto.getLongitude());
        return storeEntity;
    }
    public StoreResponseDto toDto(StoreEntity storeEntity) {
            return StoreResponseDto.builder()
                    .id(storeEntity.getId())
                    .name(storeEntity.getName())
                    .description(storeEntity.getDescription())
                    .address(storeEntity.getAddress())
                    .latitude(storeEntity.getLatitude())
                    .longitude(storeEntity.getLongitude())
                    .approvalStatus(storeEntity.getApprovalStatus())
                    .bannerImageUrl(storeEntity.getBannerImageUrl())
                    .bannerImageId(storeEntity.getBannerImageId())
                    .createdAt(storeEntity.getCreatedAt())
                    .updatedAt(storeEntity.getUpdatedAt())
                    .build();
    }

}
