package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ClientProfileCounts;

public interface ClientAndServiceProviderProfileCountService {
    ClientProfileCounts getCounts(String clientId);
}
