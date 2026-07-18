package lk.workbridge.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientProfileUpdate extends BaseProfileUpdate{

    private String organizationName;
}
