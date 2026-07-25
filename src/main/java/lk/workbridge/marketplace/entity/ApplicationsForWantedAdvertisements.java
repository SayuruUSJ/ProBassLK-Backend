package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_providers_requests_for_wanted_advertisements")
public class ApplicationsForWantedAdvertisements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false, unique = true)
    private int requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private ServiceWantedAdvertisement advertisement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "daily_rate", nullable = false)

    private Double proposedRate;

    @Enumerated(
            EnumType.STRING
    )
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;


}
