package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ClientBookingRequestAD;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.entity.HireRequest;

import java.util.List;

public interface HireRequestService {

    String requestAdvertisement(ClientBookingRequestAD requestAD);
    Boolean acceptOrReject(String advertisementId, String status);
    List<HireRequest> getAllRequestsByWorkerId(String workerId);
    Boolean updateCompleteOrIncompleteJobs(String advertisementId, String status);
   String cancelRequest(String id);
    List<ClientJobs> getClientOngoingJobs(String clientId,String jobStatus);


}
