package lk.workbridge.marketplace.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBookingRequestAD {


@NotBlank(message = "Worker ID is required")
    private String workerId;

@NotBlank(message = "Worker email is required")
    private String workerEmail;

@NotBlank(message = "Requested service is required")
    private String requestedService;

@NotBlank(message = "Advertisement ID is required")
private String advertisementId;

@NotNull(message = "Requested date is required")
@FutureOrPresent(message = "Required date cannot be in the past")
    private LocalDate requestedDate;

@NotBlank(message = "Client ID is required")
    private String clientId;

    private String clientName;

@NotBlank(message = "Client contact number is required")
    private String clientContactNumber;

@NotBlank(message = "Location is required")
    private String location;

    private String description;
 @NotBlank(message = "Status is required")
    private String status;
}
