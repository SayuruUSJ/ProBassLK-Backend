package lk.workbridge.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseProfileUpdate {

    private String userId;

    private String phoneNumber;

    private String secondaryPhoneNumber;

    private String landlineNumber;

    private String whatsappNumber;

    private String district;

    private String address;

    private String city;


}
