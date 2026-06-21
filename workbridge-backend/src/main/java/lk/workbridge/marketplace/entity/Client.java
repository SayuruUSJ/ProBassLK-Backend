package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User{
    @Column(name = "organization_name", nullable = false, length = 100)
    private String organizationName;
    @Column(name = "verification_status")
    private Boolean verificationStatus;
}
