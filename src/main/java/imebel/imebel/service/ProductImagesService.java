package imebel.imebel.service;

import imebel.imebel.dto.response.ProductImageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductImagesService {
    void deleteProductImage(UUID productId, UUID imageId);
    ProductImageResponseDto createProductImage(UUID productId, MultipartFile file);
    List<ProductImageResponseDto> getProductImage(UUID productId);
    List<ProductImageResponseDto> changeMainImage(UUID productId, UUID imageId);
}
