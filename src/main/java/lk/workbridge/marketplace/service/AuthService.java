package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.ClientProfileUpdate;
import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.ServiceProviderProfileUpdate;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.responses.ClientProfile;
import lk.workbridge.marketplace.dto.responses.ServiceProviderProfile;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    String register(RegisterRequest request);

    UserDetails loadUserByUsername(String username);

    User getCurrentUser();

    boolean verifyUser(VerificationRequest request);

    String clientProfileUpdate(ClientProfileUpdate clientProfileUpdate);

    String serviceProviderProfileUpdate(ServiceProviderProfileUpdate serviceProviderProfileUpdate);

    String uploadProfileImage(String profileImageUrl, String userId);

    ClientProfile getClientProfileInfo(String userId);

    ServiceProviderProfile getServiceProviderProfileInfo(String userId);


}
