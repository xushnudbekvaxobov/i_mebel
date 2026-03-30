package imebel.imebel.service;

import imebel.imebel.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    void createCategory(String name);
    CategoryResponseDto  updateCategory(UUID id, String name);
    CategoryResponseDto getCategoryById(UUID id);
    void deleteCategory(UUID id);
}
