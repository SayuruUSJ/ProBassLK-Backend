package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.HireRequestAD;
import lk.workbridge.marketplace.dto.responses.ClientHireRequestResponse;
import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.HireRequestCreatedResponse;
import lk.workbridge.marketplace.dto.responses.HireRequestResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HireRequestService {

    HireRequestCreatedResponse requestAdvertisement(HireRequestAD requestAD);

    Boolean acceptOrReject(String advertisementId, String status);

    List<HireRequestResponse> getAllRequestsByWorkerId(String workerId);

    List<HireRequestResponse> getAllPendingRequestsByWorkerId(String workerId);

    List<HireRequestResponse> getAllAcceptedRequestsByWorkerId(String workerId);

    List<HireRequestResponse> getAllRejectedRequestsByWorkerId(String workerId);

    Boolean updateCompleteOrIncompleteJobs(String advertisementId, String status);

    String cancelRequest(String id);

    List<ClientJobs> getClientOngoingJobs(String clientId, String jobStatus);

    Page<ClientHireRequestResponse> getClientAllHireRequests(String clientId, int page, int size);


}
