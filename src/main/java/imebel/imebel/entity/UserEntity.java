package imebel.imebel.entity;

import imebel.imebel.util.enums.AuthProvider;
import imebel.imebel.util.enums.UserRole;
import imebel.imebel.util.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,updatable = false)
    private Long id;
    @OneToOne(mappedBy = "userEntity")
    private EmailVerificationEntity emailVerificationEntity;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phone;
    @Column(nullable = false,unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    private String providerId;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "profile_image")
    private String profileImage;
    @Column(name = "address", length = 255)
    private String address;
    @Column( nullable = false,updatable = false)
    private LocalDate createdAt;
    @Column( nullable = false)
    private LocalDate updatedAt;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(nullable = false)
    private Boolean isAccountNonExpired;
    @Column(nullable = false)
    private Boolean isAccountNonLocked;
    @Column(nullable = false)
    private Boolean isCredentialsNonExpired;
    @Column(nullable = false)
    private Boolean isEnabled;

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
