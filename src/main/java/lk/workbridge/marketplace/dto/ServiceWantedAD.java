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
public class ServiceWantedAD {

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Client contact number is required")
    private String clientContactNumber;

    private String description;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Required date is required")
    @FutureOrPresent(message = "Required date cannot be in the past")
    private LocalDate requiredDate;

    @NotBlank(message = "Status is required")
    private String status;
}
