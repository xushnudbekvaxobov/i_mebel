package imebel.imebel.service.serviceImpl;

import com.cloudinary.Cloudinary;
import imebel.imebel.dto.response.ProductImageResponseDto;
import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.ProductImageEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.mapper.ProductImageMapper;
import imebel.imebel.repository.ProductImageRepository;
import imebel.imebel.repository.ProductRepository;
import imebel.imebel.service.ProductImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImagesServiceImpl implements ProductImagesService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final Cloudinary  cloudinary;
    private final ProductImageMapper productImageMapper;

    @Override
    public void deleteProductImage(UUID productId, UUID imageId) {
        productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("product not found"));
        long imageCount = productImageRepository.countImagesByProductId(productId);
        if (imageCount < 2) {
            throw new AppBadException("Product must have at least one image");
        }
        ProductImageEntity productImageEntity = productImageRepository.findById(imageId).orElseThrow(() -> new DataNotFoundException("image not found"));
        if (productImageEntity.isMain()) {
            ProductImageEntity productImage = productImageRepository.findFirstByProductEntity_IdAndIdNot(productId, productImageEntity.getId()).orElseThrow(() -> new DataNotFoundException("image not found"));
            productImage.setMain(true);
            productImageRepository.save(productImage);
        }
        try {
            cloudinary.uploader().destroy(productImageEntity.getPublicId(), Map.of("invalidate", true));
        }catch (IOException e){
            throw new AppBadException("Cloudinary image delete failed.");
        }
        productImageRepository.delete(productImageEntity);
    }

    @Override
    public ProductImageResponseDto createProductImage(UUID productId, MultipartFile file) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("product not found"));
        if (productImageRepository.countImagesByProductId(productId) >= 3) {
            throw new AppBadException("Product must have at most three image");
        }
        ProductsServiceImpl.validateImage(file);
        ProductImageResponseDto productImageResponseDto;
        try {
            Map<String, String> options = new HashMap<>();
            options.put("folder", "imebel/products");
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            ProductImageEntity productImageEntity = ProductImageEntity.builder()
                    .imageUrl( uploadResult.get("secure_url").toString())
                    .publicId(uploadResult.get("public_id").toString())
                    .productEntity(productEntity)
                    .isMain(false)
                    .build();
            productImageRepository.save(productImageEntity);
            productImageResponseDto = productImageMapper.toDto(productImageEntity);
        } catch (IOException e) {
            throw new AppBadException("Cloudinary image upload failed.");
        }
        return productImageResponseDto;
    }

    @Override
    public List<ProductImageResponseDto> getProductImage(UUID productId) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("product not found"));
        List<ProductImageEntity> productImageEntity = productEntity.getProductImages();
        return productImageEntity.stream().map(productImageMapper::toDto).toList();
    }

    @Override
    @Transactional
    public List<ProductImageResponseDto> changeMainImage(UUID productId, UUID imageId) {
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new DataNotFoundException("product not found"));
        List<ProductImageEntity> productImageEntity = productEntity.getProductImages();
        for (ProductImageEntity productImageEntity1 : productImageEntity) {
            productImageEntity1.setMain(false);
        }
        ProductImageEntity targetImage = productImageEntity.stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("image not found"));
        targetImage.setMain(true);
        return productImageRepository.saveAll(productImageEntity).stream().map(productImageMapper::toDto).toList();
    }

}
