package imebel.imebel.service;

import imebel.imebel.dto.request.ProductCreateDto;
import imebel.imebel.dto.response.PageResponse;
import imebel.imebel.dto.response.ProductResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProductsService {
    ProductResponseDto createProducts(ProductCreateDto productCreateDto, List<MultipartFile> images, String email);
    PageResponse<ProductResponseDto> getMyProducts(int page, int size, String email);
    ProductResponseDto getProductById(UUID id);
    ProductResponseDto updateProduct(UUID id, ProductCreateDto productCreateDto);
    void deleteProduct(UUID id);
}
