package lk.workbridge.marketplace.service;

import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.UpdateProfile;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.responses.UserProfile;
import lk.workbridge.marketplace.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
public interface AuthService {

    String register(RegisterRequest request);
    UserDetails loadUserByUsername(String username);
    User getCurrentUser();
    boolean verifyUser(VerificationRequest request);
    String updateProfile(UpdateProfile updateProfile);
    String uploadProfileImage(String  profileImageUrl,String userId);
    UserProfile getProfileInfo(String userId);
}
