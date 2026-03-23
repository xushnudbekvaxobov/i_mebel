package imebel.imebel.repository;

import imebel.imebel.entity.StoreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    Optional<StoreEntity> findByUserEntity_Email(String email);

    @Query(value = """
        SELECT DISTINCT s.*
        FROM stores s
        LEFT JOIN store_categories sc ON sc.store_id = s.id
        WHERE (:name IS NULL OR s.name ILIKE CONCAT('%', :name, '%'))
          AND (:categoryId IS NULL OR sc.category_id = :categoryId)
        """,
            nativeQuery = true)
    List<StoreEntity> searchStores(
                            @Param("name") String name,
                            @Param("categoryId") Long categoryId);
}
