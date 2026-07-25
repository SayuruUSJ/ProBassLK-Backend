package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.responses.ClientMoreInfo;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.dto.responses.UserBasicInfo;
import lk.workbridge.marketplace.dto.responses.WorkerMoreInfo;
import org.springframework.data.domain.Page;

public interface AdminService {

    boolean acceptOrRejectServiceProviderAdvertisement(String serviceId, String status);

    boolean acceptOrRejectServiceWantedAdvertisement(String advertisementId, String status);

    Page<ServiceProviderADResponse> getAllPendingServiceProviderAdvertisements(int page, int size);

    Page<ServiceWantedADResponse> getAllPendingWantedAdvertisements(int page, int size);

    String deleteServiceProviderAdvertisement(String serviceId);

    String deleteServiceWantedAdvertisement(String advertisementId);

    String deleteUser(String userId);

    Page<UserBasicInfo> getAllUsersBaseInfo(int page, int size);

    WorkerMoreInfo getWorkerMoreInfo(String workerId);

    ClientMoreInfo getClientMoreInfo(String clientId);

    Page<ServiceProviderADResponse> getAllServiceProviderAdvertisements(int page, int size);

    Page<ServiceWantedADResponse> getAllServiceWantedAdvertisements(int page, int size);
}
