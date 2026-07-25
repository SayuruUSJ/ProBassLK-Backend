package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lk.workbridge.marketplace.enums.ContactMethod;
import lk.workbridge.marketplace.enums.PaymentType;
import lk.workbridge.marketplace.util.ServiceWantedADID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Enumerated(
            EnumType.STRING
    )
    @Column(name = "preferred_contact_method")
    private ContactMethod preferredContactMethod;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "service_type", nullable = false, length = 100)
    private String serviceType;

    @Column(name = "location", nullable = false, length = 255)
    private String fullAddress;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "required_date", nullable = false)
    private LocalDate requiredDate;

    @Column(name = "start_time", nullable = true)
    private LocalTime startTime;

    @Column(name = "expected_duration", nullable = false)
    private String expectedDuration;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "is_work_date_flexible")
    private boolean isWorkDateFlexible;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "required_skills", nullable = true)
    private String requiredSkills;

    @Column(name = "no_of_workers_required", nullable = false)
    private int noOfWorkersRequired;
    @Enumerated(
            EnumType.STRING
    )
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "offered_rate", nullable = false)
    private Double offeredRate;

    @Column(name = "is_negotiable", nullable = false)
    private boolean isRateNegotiable;

    @Column(name = "isUrgent", nullable = true)
    private boolean isUrgent;

    @Column(name = "additional_instructions", nullable = true)
    private String additionalInstructions;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
