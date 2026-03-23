package imebel.imebel.entity;

import imebel.imebel.util.enums.OrderStatus;
import imebel.imebel.util.enums.UrgencyType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private UserEntity master;
    @Column(name = "category", nullable = false, length = 100)
    private String category;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Column(name = "address", nullable = false, length = 255)
    private String address;
    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;
    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;
    @Column(name = "price_min", precision = 12, scale = 2)
    private BigDecimal priceMin;
    @Column(name = "price_max", precision = 12, scale = 2)
    private BigDecimal priceMax;
    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", length = 20)
    private UrgencyType urgency;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderAttachmentEntity> attachments = new ArrayList<>();
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ReviewEntity review;
}
