package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ClientJobs;
import lk.workbridge.marketplace.dto.responses.ServiceProviderJobs;

import java.util.List;

public interface ServiceProviderJobService {

    List<ServiceProviderJobs> getServiceProviderOngoingJobs(String serviceProviderId, String jobStatus);
    List<ServiceProviderJobs> getServiceProviderCompletedJobs(String serviceProviderId,String jobStatus);
    List<ServiceProviderJobs> getServiceProviderCancelledJobs(String serviceProviderId,String jobStatus);
}
