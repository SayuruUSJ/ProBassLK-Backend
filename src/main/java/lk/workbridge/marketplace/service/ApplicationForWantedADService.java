package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.dto.responses.WantedAdvertisementApplication;
import org.springframework.data.domain.Page;

public interface ApplicationForWantedADService {
    String createNewRequest(ServiceProviderRequestForWantedAD requestForWantedAD);
    String updateRequest(String ad_status,int request_id,String request_status);
    Page<WantedAdvertisementApplication> getClientApplications(String clientId,int page,int size);
}
