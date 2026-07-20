package lk.workbridge.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name="application_requests_cancellaion_results")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestCancellationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cancellationResultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private ApplicationsForWantedAdvertisements request;


    private String message;

    private  String status;

    private LocalDateTime cancelledAt;

    private long daysUntilService;
}
