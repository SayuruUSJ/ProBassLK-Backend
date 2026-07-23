package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ServiceProviderJobs;

import java.util.List;

public interface ServiceProviderJobService {

    List<ServiceProviderJobs> getServiceProviderOngoingJobs(String serviceProviderId);
    List<ServiceProviderJobs> getServiceProviderCompletedJobs(String serviceProviderId);
    List<ServiceProviderJobs> getServiceProviderCancelledJobs(String serviceProviderId);
    String handleServiceProviderAndAdvertisementAvailability(String serviceProviderId,String hireRequestId);
    String handleServiceWantedAdvertisement(int requestId,String serviceProviderId);
}
