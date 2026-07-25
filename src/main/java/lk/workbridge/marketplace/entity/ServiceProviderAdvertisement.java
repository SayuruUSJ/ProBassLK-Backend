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

    @OneToOne  // Add this annotation
    @JoinColumn(name = "worker_id", referencedColumnName = "user-id", nullable = false)
    private Worker worker;


    private String status;


    private LocalDate createdAt;

    private LocalDate updatedAt;
//    @Column(name="service_provider_id",nullable = false)
//    private String serviceProviderId;
//@Column(name="service_provider_name",nullable = false)
//    private String serviceProviderName;
//
//@Column(name="contact_number",nullable = false)
//    private String contactNumber;
//
//    @OneToMany(
//            mappedBy = "worker",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
//@Column(name="skills",nullable = false)
//
//    private Set<WorkerSkill> skills
//            = new HashSet<>();
//
//    private Boolean available;
//
//    private String address;


}
