package lk.workbridge.marketplace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderAD {

    @NotNull(message = "Worker is required")
    private String workerId;


    private String status;
}
