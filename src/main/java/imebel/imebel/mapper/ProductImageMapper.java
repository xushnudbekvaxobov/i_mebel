package imebel.imebel.mapper;

import imebel.imebel.dto.request.ProductImageCreateDto;
import imebel.imebel.dto.response.ProductImageResponseDto;
import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.ProductImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {
    public ProductImageEntity toEntity(ProductEntity productEntity, ProductImageCreateDto  productImageCreateDto) {
            return ProductImageEntity.builder()
                    .imageUrl(productImageCreateDto.getImageUrl())
                    .publicId(productImageCreateDto.getPublicId())
                    .isMain(productImageCreateDto.isMain())
                    .productEntity(productEntity)
                    .build();
    }
    public ProductImageResponseDto toDto(ProductImageEntity productImageEntity) {
        return ProductImageResponseDto.builder()
                .id(productImageEntity.getId())
                .url(productImageEntity.getImageUrl())
                .isMain(productImageEntity.isMain())
                .build();
    }
}
