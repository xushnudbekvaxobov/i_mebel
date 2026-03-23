package imebel.imebel.service;

import imebel.imebel.dto.request.ProductCreateDto;
import imebel.imebel.dto.response.PageResponse;
import imebel.imebel.dto.response.ProductResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductsService {
    ProductResponseDto createProducts(ProductCreateDto productCreateDto, List<MultipartFile> images);
    PageResponse<ProductResponseDto> getMyProducts(int page, int size);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto updateProduct(Long id, ProductCreateDto productCreateDto);
    void deleteProduct(Long id);
}
