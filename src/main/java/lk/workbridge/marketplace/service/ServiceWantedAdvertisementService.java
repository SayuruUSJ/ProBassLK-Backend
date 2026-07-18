package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceWantedAD;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import org.springframework.data.domain.Page;

public interface ServiceWantedAdvertisementService {

    String requestAdvertisement(ServiceWantedAD serviceWantedAD);
    Page<ServiceWantedADResponse> getAllAdvertisements(int page, int size);
}
