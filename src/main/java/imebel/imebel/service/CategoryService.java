package imebel.imebel.service;

import imebel.imebel.dto.response.CategoryResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    void createCategory(String name);
    CategoryResponseDto  updateCategory(Long id, String name);
    CategoryResponseDto getCategoryById(Long id);
}
