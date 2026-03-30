package imebel.imebel.entity;

import imebel.imebel.util.enums.CategoryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    private CategoryStatus status;
}
