package imebel.imebel.repository;

import imebel.imebel.entity.StoreEntity;
import imebel.imebel.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAllByStoreEntity(StoreEntity storeEntity, Pageable pageable);

    Page<ProductEntity> getAllByStoreEntity(StoreEntity masterProfile, Pageable pageable);
}
