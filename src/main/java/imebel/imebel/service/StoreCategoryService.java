package imebel.imebel.service;

import imebel.imebel.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StoreCategoryService {
    List<CategoryResponseDto> getMyCategories();
    void createMyCategory(String name);
    void deleteMyCategory(String name);
    List<CategoryResponseDto> getAllStoreCategories(Long storeId);
}
