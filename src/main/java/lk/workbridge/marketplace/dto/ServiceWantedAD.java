package lk.workbridge.marketplace.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.workbridge.marketplace.enums.ContactMethod;
import lk.workbridge.marketplace.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWantedAD {

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Client contact number is required")

    private String clientContactNumber;

    @NotNull(message = "Preferred contact method is required")
    private ContactMethod preferredContactMethod;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotBlank(message = "Service type is required")

    private String serviceType;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "District is required")

    private String district;

    @NotNull(message = "Required date is required")
    @FutureOrPresent(message = "Required date cannot be in the past")
    private LocalDate requiredDate;

    private LocalTime startTime;

    @NotBlank(message = "Expected duration is required")
    private String expectedDuration;

    @NotNull(message = "Application deadline is required")
    @FutureOrPresent(message = "Application deadline cannot be in the past")
    private LocalDate applicationDeadline;

    @NotNull(message = "Work date flexibility is required")
    private Boolean isWorkDateFlexible;

    @NotBlank(message = "Status is required")

    private String status;

    @Size(max = 255, message = "Required skills must not exceed 255 characters")
    private String requiredSkills;

    @NotNull(message = "Number of workers required is required")
    private int noOfWorkersRequired;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotNull(message = "Offered rate is required")
    private Double offeredRate;

    @NotNull(message = "Rate negotiability is required")
    private Boolean isRateNegotiable;

    @Size(max = 255, message = "Additional instructions must not exceed 255 characters")
    private String additionalInstructions;

    private boolean isUrgent;

}