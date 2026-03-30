package imebel.imebel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageEntity extends BaseEntity {
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String publicId;
    @Column(nullable = false)
    private boolean isMain;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;
}
