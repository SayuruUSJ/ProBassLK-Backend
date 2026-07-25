package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ClientJobs;

import java.util.List;

public interface ClientJobsService {

    List<ClientJobs> getClientOngoingJobs(String clientId);

    List<ClientJobs> getClientCompletedJobs(String clientId);

    List<ClientJobs> getClientCancelledJobs(String clientId);
}
