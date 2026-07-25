package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import org.springframework.data.domain.Page;


public interface ServiceProviderAdvertisementService {
    String createNewAdvertisement(ServiceProviderAD serviceProviderAD);

    //    Boolean updateAdvertisementStatus(String status,String serviceId);
    ServiceProviderADResponse getAdvertisementForSpecificWorker(String workerId);

    Page<ServiceProviderADResponse> getAllAdvertisements(int page, int size);
    //Page<ServiceProviderADResponse> getAllPendingAdvertisements(int page, int size);
}
