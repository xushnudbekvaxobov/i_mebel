package imebel.imebel.service;

import imebel.imebel.dto.response.ProductImageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProductImagesService {
    void deleteProductImage(Long productId, Long imageId);
    ProductImageResponseDto createProductImage(Long productId, MultipartFile file);
}
