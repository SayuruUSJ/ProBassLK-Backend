package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.util.ServiceWantedADID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "service_wantedAdvertisements")
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWantedAdvertisement {

    @Id
    @ServiceWantedADID
    @Column(name = "advertisement_id", nullable = false, unique = true)
    private String advertisement_id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "user-id", nullable = false)
    private Client client;

    @Column(name = "client_contact_number", nullable = false, length = 100)
    private String clientContactNumber;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name="service_type", nullable = false, length = 100)
    private String serviceType;

    @Column(name = "location", nullable = false, length = 255)
    private String location;

    @Column(name = "required_date", nullable = false)
    private LocalDate requiredDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;


}
