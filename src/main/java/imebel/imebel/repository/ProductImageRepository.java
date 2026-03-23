package imebel.imebel.repository;

import imebel.imebel.entity.ProductEntity;
import imebel.imebel.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {

    List<ProductImageEntity> findByProductEntity(ProductEntity productEntity);

    @Query(value = "select count(*) from product_images where product_id =:productId", nativeQuery = true)
    long countImagesByProductId(@Param("productId") Long productId);
    Optional<ProductImageEntity> findFirstByProductEntity_IdAndIdNot(Long productId, Long imageId);
}
