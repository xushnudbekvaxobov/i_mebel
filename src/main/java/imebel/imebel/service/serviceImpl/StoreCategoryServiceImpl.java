package imebel.imebel.service.serviceImpl;

import imebel.imebel.dto.response.CategoryResponseDto;
import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.StoreCategoriesEntity;
import imebel.imebel.entity.CategoryEntity;
import imebel.imebel.exception.AppBadException;
import imebel.imebel.exception.DataNotFoundException;
import imebel.imebel.mapper.CategoryMapper;
import imebel.imebel.repository.StoreRepository;
import imebel.imebel.repository.StoreCategoryRepository;
import imebel.imebel.repository.CategoryRepository;
import imebel.imebel.service.StoreCategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreCategoryServiceImpl implements StoreCategoryService {


    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final CategoryMapper categoryMapper;

    public StoreCategoryServiceImpl(StoreRepository storeRepository, CategoryRepository categoryRepository, StoreCategoryRepository storeCategoryRepository, CategoryMapper categoryMapper) {
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.storeCategoryRepository = storeCategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponseDto> getMyCategories() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(() -> new DataNotFoundException("user profile not found"));
        List<StoreCategoriesEntity> categoryEntity = storeEntity.getStoreCategoryList();
        return categoryEntity.stream().map(StoreCategoriesEntity::getCategory).map(categoryMapper::toDto).toList();
    }
    @Override
    public void createMyCategory(String name) {
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("user profile not found"));
        CategoryEntity categoryEntity = categoryRepository.findByName(name).orElseThrow(()-> new DataNotFoundException("specialization not found"));
        Optional<StoreCategoriesEntity> masterSpecializationEntity = storeCategoryRepository.findByStoreAndCategory(storeEntity, categoryEntity);
        if (masterSpecializationEntity.isPresent()) {
            throw new AppBadException("specialization already exists");
        }
        StoreCategoriesEntity storeCategoryEntity = new StoreCategoriesEntity();
        storeCategoryEntity.setCategory(categoryEntity);
        storeCategoryEntity.setStore(storeEntity);
        storeCategoryRepository.save(storeCategoryEntity);
    }

    @Override
    public void deleteMyCategory(String name) {
        Authentication  authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        StoreEntity storeEntity = storeRepository.findByUserEntity_Email(email).orElseThrow(()-> new DataNotFoundException("user profile not found"));
        StoreCategoriesEntity storeCategoriesEntity = storeCategoryRepository.findByStoreAndCategory_Name(storeEntity, name).orElseThrow(()-> new DataNotFoundException("specialization not found"));
        storeCategoryRepository.delete(storeCategoriesEntity);
    }

    @Override
    public List<CategoryResponseDto> getAllStoreCategories(Long storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId).orElseThrow(()-> new DataNotFoundException("user profile not found"));
        List<StoreCategoriesEntity> storeCategoriesEntities = storeEntity.getStoreCategoryList();
        return storeCategoriesEntities.stream().map(StoreCategoriesEntity::getCategory)
                .map(categoryMapper::toDto).toList();
    }

}
