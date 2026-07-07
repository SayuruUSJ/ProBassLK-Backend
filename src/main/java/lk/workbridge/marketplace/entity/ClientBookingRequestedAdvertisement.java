package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.util.ServiceProviderBookingADID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client_requestedAdvertisements")
public class ClientBookingRequestedAdvertisement {

    @Id
    @ServiceProviderBookingADID
    @Column(name = "advertisement_id", nullable = false, length = 20)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", referencedColumnName = "user-id", nullable = false)
    private Worker worker;

    @Column(name = "worker_email", nullable = false, length = 100)
    private String workerEmail;

    @Column(name = "requested_service", nullable = false, length = 100)
    private String requestedService;

    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "user-id", nullable = false)
    private Client client;

    @Column(name = "client_name", nullable = true, length = 100)
    private String clientName;

    @Column(name = "client_contact_number", nullable = true, length = 20)
    private String clientContactNumber;

    @Column(name = "location", nullable = true, length = 100)
    private String location;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

}
