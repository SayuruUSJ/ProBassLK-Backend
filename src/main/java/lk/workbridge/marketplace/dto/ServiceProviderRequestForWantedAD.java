package lk.workbridge.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderRequestForWantedAD {

    @NotNull(message = "Advertisement ID is required")
    private String advertisement_id;

    @NotNull(message = "Worker ID is required")

    private String workerId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Daily rate is required")
    private Double dailyRate;

    @NotBlank(message = "Status is required")
    private String status;
}
