package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ClientBookingRequestAD;
import lk.workbridge.marketplace.entity.ClientBookingRequestedAdvertisement;

import java.util.List;

public interface ClientBookingRequestAdvertisementService {

    String requestAdvertisement(ClientBookingRequestAD requestAD);
    Boolean acceptOrReject(String advertisementId, String status);
    List<ClientBookingRequestedAdvertisement> getAllRequestsByWorkerId(String workerId);
}
