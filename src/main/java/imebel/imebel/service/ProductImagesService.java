package imebel.imebel.service;

import imebel.imebel.dto.response.ProductImageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductImagesService {
    void deleteProductImage(Long productId, Long imageId);
    ProductImageResponseDto createProductImage(Long productId, MultipartFile file);
    List<ProductImageResponseDto> getProductImage(Long productId);
    List<ProductImageResponseDto> changeMainImage(Long productId, Long imageId);
}
