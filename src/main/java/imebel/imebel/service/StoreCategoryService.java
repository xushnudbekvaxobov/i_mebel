package imebel.imebel.service;

import imebel.imebel.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface StoreCategoryService {
    List<CategoryResponseDto> getMyCategories(String email);
    void createMyCategory(String name);
    void deleteMyCategory(String name);
    List<CategoryResponseDto> getAllStoreCategories(UUID storeId);
}
