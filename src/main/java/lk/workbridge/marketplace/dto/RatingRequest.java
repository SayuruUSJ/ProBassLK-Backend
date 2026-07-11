package lk.workbridge.marketplace.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {

    @NotNull(message = "Worker ID is required")
    private String workerId;

    @NotNull(message = "Stars is required")
    private int stars;

    @NotNull(message = "Client ID is required")
    private String clientId;

    private String comment;

}
