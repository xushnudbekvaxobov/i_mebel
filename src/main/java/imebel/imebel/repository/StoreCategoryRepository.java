package imebel.imebel.repository;

import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.StoreCategoriesEntity;
import imebel.imebel.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreCategoryRepository extends JpaRepository<StoreCategoriesEntity, UUID> {
    Optional<StoreCategoriesEntity> findByStoreAndCategory(StoreEntity masterProfile, CategoryEntity categoryEntity);
    Optional<StoreCategoriesEntity> findByStoreAndCategory_Name(StoreEntity masterProfile, String name);
}
