package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;

public interface ServiceProviderRequestForWantedADService {
    String createNewRequest(ServiceProviderRequestForWantedAD requestForWantedAD);
    String updateRequest(String ad_status,int request_id,String request_status);
}
