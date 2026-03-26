package imebel.imebel.mapper;

import imebel.imebel.dto.response.CategoryResponseDto;
import imebel.imebel.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponseDto toDto(CategoryEntity categoryEntity) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(categoryEntity.getId());
        categoryResponseDto.setName(categoryEntity.getName());
        categoryResponseDto.setStatus(categoryEntity.getStatus());
        return categoryResponseDto;
    }
}
