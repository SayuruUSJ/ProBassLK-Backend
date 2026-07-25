package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.WantedAdvertisementApplication;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ApplicationForWantedADService {
    String createNewRequest(ServiceProviderRequestForWantedAD requestForWantedAD);

    String updateRequest(String ad_status, int request_id, String request_status);

    Page<WantedAdvertisementApplication> getClientApplications(String clientId, int page, int size);

    List<ClientJobs> getClientOngoingApplications(String clientId, String jobStatus);

    Boolean updateCompleteOrIncompleteJobs(int applicationId, String status);

    String cancelRequest(int id);

    Page<WantedAdvertisementApplication> getServiceProvidersSpecificApplications(String serviceProviderId, int page, int size);

}
