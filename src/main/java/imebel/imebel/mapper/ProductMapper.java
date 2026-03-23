package imebel.imebel.mapper;

import imebel.imebel.dto.request.ProductCreateDto;
import imebel.imebel.dto.response.ProductResponseDto;
import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.CategoryEntity;
import imebel.imebel.util.enums.ProductStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final ProductImageMapper productImageMapper;

    public ProductMapper(ProductImageMapper productImageMapper) {
        this.productImageMapper = productImageMapper;
    }

    public ProductEntity toEntity(StoreEntity storeEntity, ProductCreateDto productCreateDto, CategoryEntity categoryEntity){
        ProductEntity productEntity = buildBaseEntity(productCreateDto, categoryEntity);
        productEntity.setStoreEntity(storeEntity);
        return productEntity;
    }

    public ProductEntity toEntity(ProductCreateDto productCreateDto, CategoryEntity categoryEntity){
        return buildBaseEntity(productCreateDto, categoryEntity);
    }
    private ProductEntity buildBaseEntity(ProductCreateDto productCreateDto, CategoryEntity categoryEntity){
        return ProductEntity.builder()
                .category(categoryEntity)
                .name(productCreateDto.getName())
                .description(productCreateDto.getDescription())
                .price(productCreateDto.getPrice())
                .material(productCreateDto.getMaterial())
                .color(productCreateDto.getColor())
                .length(productCreateDto.getLength())
                .width(productCreateDto.getWidth())
                .height(productCreateDto.getHeight())
                .status(ProductStatus.ACTIVE)
                .build();
    }

    public ProductResponseDto toDto(ProductEntity productEntity){
        return ProductResponseDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .material(productEntity.getMaterial())
                .color(productEntity.getColor())
                .length(productEntity.getLength())
                .width(productEntity.getWidth())
                .height(productEntity.getHeight())
                .status(productEntity.getStatus())
                .imageList(productEntity.getProductImages().stream().map(productImageMapper::toDto).toList())
                .build();
    }
}
