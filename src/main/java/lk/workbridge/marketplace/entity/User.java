package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.enums.Role;
import lk.workbridge.marketplace.util.WorkbridgeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;



import java.util.Date;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract  class User {

    @Id
    @WorkbridgeId
    @Column(name = "user-id", length = 50)
    private String id;
    @Column(name = "username", unique = true, nullable = false, length = 50)
    private  String username;
    @Column(name = "password", nullable = false, length = 100)
    private String password;
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;
    @Column(name = "phone-number", length = 12,nullable = true)
    private String phoneNumber;
    @Column(name = "first-name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last-name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "district", nullable = true, length = 50)
    private String district;
    @Column(name = "address", nullable = true, length = 100)
    private String address;
    @Enumerated(
            EnumType.STRING
    )
    @Column(name="role",nullable = false)
    private Role role;
    @CreationTimestamp
    @Column(name = "created-at", nullable = false)
     private Date createdAt;
    @Column(name = "verification_status")
    private Boolean verificationStatus;
    @Column(name="profile_image_url",nullable = true)
    private String profileImageUrl;
}
