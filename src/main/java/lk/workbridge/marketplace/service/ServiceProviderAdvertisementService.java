package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.ServiceProviderADResponse;
import org.springframework.stereotype.Service;


public interface ServiceProviderAdvertisementService {
    String createNewAdvertisement(ServiceProviderAD serviceProviderAD);
    Boolean updateAdvertisementStatus(String status,String serviceId);
    ServiceProviderADResponse getAdvertisementForSpecificWorker(String serviceId);
}
