package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.util.ServiceProviderAdvertisementID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_provider_advertisement")
public class ServiceProviderAdvertisement {

    @Id
    @ServiceProviderAdvertisementID
    @Column(name = "service_id", nullable = false, unique = true)
    private String serviceId;

    @OneToOne  
    @JoinColumn(name = "worker_id", referencedColumnName = "user-id", nullable = false)
    private Worker worker;


    private String status;


    private LocalDate createdAt;

    private LocalDate updatedAt;


}
