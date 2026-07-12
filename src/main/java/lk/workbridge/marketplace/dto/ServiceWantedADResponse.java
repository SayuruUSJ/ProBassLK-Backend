package lk.workbridge.marketplace.dto;

import jakarta.persistence.Column;
import lk.workbridge.marketplace.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWantedADResponse {

    private String firstName;

    private String lastName;

    private String title;

    private String clientContactNumber;

    private String description;

    private String serviceType;

    private String location;

    private LocalDate requiredDate;

    private String status;

    private Long applicationCount;
}
