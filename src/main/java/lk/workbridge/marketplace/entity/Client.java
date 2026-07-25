package lk.workbridge.marketplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User {
    @Column(name = "organization_name", nullable = true, length = 100)
    private String organizationName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<HireRequest> bookingRequests = new HashSet<>();


}
