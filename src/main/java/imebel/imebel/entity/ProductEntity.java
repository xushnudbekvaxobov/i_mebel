package imebel.imebel.entity;

import imebel.imebel.util.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity storeEntity;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
    @Column(name = "name", nullable = false, length = 150)
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(name = "material", length = 150)
    private String material;
    @Column(name = "color", length = 100)
    private String color;
    @Column(nullable = false, length = 100)
    private Double length;
    @Column(nullable = false, length = 100)
    private Double width;
    @Column(nullable = false, length = 100)
    private Double height;
    @Column(name = "average_rating")
    private Double averageRating;
    @Column(name = "review_count")
    private Integer reviewCount;
    @OneToMany(mappedBy = "productEntity")
    private List<ProductImageEntity> productImages;
    @Column(nullable = false)
    private ProductStatus status;
}
