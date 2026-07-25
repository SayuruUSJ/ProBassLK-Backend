package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ClientProfileCounts;
import lk.workbridge.marketplace.dto.responses.ServiceProviderProfileCounts;

public interface ClientAndServiceProviderProfileCountService {
    ClientProfileCounts getCounts(String clientId);

    ServiceProviderProfileCounts getServiceProviderProfileCounts(String workerId);
}
