package imebel.imebel.service.serviceImpl;

import imebel.imebel.dto.response.CategoryResponseDto;
import imebel.imebel.entity.CategoryEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.repository.CategoryRepository;
import imebel.imebel.service.CategoryService;
import imebel.imebel.util.enums.CategoryStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        List<CategoryEntity> categoryEntity = categoryRepository.findAll();
        return categoryEntity.stream().map(category-> new CategoryResponseDto(category.getId(), category.getName(), category.getStatus())).toList();
    }

    @Override
    public void createCategory(String name) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findByName(name);
        if (categoryEntity.isPresent()) {
            throw new AppBadException("Category with name " + name + " already exists");
        }
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setName(name);
        categoryEntity1.setStatus(CategoryStatus.ACTIVE);
        categoryRepository.save(categoryEntity1);
    }

    @Override
    public CategoryResponseDto updateCategory(UUID id, String name) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category with id " + id + " does not exist"));
        categoryEntity.setName(name);
       CategoryEntity savedCategory =  categoryRepository.save(categoryEntity);
        return new CategoryResponseDto(savedCategory.getId(),savedCategory.getName(),  savedCategory.getStatus());
    }

    @Override
    public CategoryResponseDto getCategoryById(UUID id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new AppBadException("Category with id " + id + " does not exist"));
        return new CategoryResponseDto(categoryEntity.getId(), categoryEntity.getName(),  categoryEntity.getStatus());
    }

    @Override
    public void deleteCategory(UUID id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Category with id " + id + " does not exist"));
        categoryEntity.setStatus(CategoryStatus.DELETED);
        categoryRepository.save(categoryEntity);
    }


}
