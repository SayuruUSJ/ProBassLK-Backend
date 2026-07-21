package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.HireRequestAD;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.HireRequestResponse;
import lk.workbridge.marketplace.entity.HireRequest;

import java.util.List;

public interface HireRequestService {

    String requestAdvertisement(HireRequestAD requestAD);
    Boolean acceptOrReject(String advertisementId, String status);
    List<HireRequestResponse> getAllRequestsByWorkerId(String workerId);
    List<HireRequestResponse> getAllPendingRequestsByWorkerId(String workerId);
    List<HireRequestResponse> getAllAcceptedRequestsByWorkerId(String workerId);
    List<HireRequestResponse> getAllRejectedRequestsByWorkerId(String workerId);
    Boolean updateCompleteOrIncompleteJobs(String advertisementId, String status);
   String cancelRequest(String id);
    List<ClientJobs> getClientOngoingJobs(String clientId,String jobStatus);


}
