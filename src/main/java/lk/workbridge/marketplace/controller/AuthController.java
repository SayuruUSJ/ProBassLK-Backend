package lk.workbridge.marketplace.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lk.workbridge.marketplace.dto.ClientProfileUpdate;
import lk.workbridge.marketplace.dto.LoginRequest;
import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.ServiceProviderProfileUpdate;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.dto.WorkerSkillRequest;
import lk.workbridge.marketplace.dto.responses.ClientProfile;
import lk.workbridge.marketplace.dto.responses.ServiceProviderProfile;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.service.AuthService;
import lk.workbridge.marketplace.service.CloudinaryService;
import lk.workbridge.marketplace.service.EmailService;
import lk.workbridge.marketplace.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;

    private final CloudinaryService cloudinaryService;

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String result = service.register(request);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/update-client-profile")
    public ResponseEntity<?> updateClientProfile(@RequestBody ClientProfileUpdate clientProfileUpdate) {

        String result = service.clientProfileUpdate(clientProfileUpdate);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-service-provider-profile")
    public ResponseEntity<?> updateServiceProviderProfile(@RequestBody ServiceProviderProfileUpdate serviceProviderProfileUpdate) {

        String result = service.serviceProviderProfileUpdate(serviceProviderProfileUpdate);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/upload-image")
    public ResponseEntity<String> upload(
            @RequestParam("image") MultipartFile image,
            @RequestParam("user-id") String userID)
            throws IOException {

        String url = cloudinaryService.uploadFile(image);
        System.out.println(userID);
        String result = service.uploadProfileImage(url, userID);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestParam String email) {
        boolean sent = emailService.sendVerificationEmail(email);
        if (sent) {
            return ResponseEntity.ok("Verification email sent");
        } else {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody VerificationRequest request) {

        boolean isValid = verificationCodeService.validateCode(request.getEmail(), request.getCode());
        if (isValid) {
            service.verifyUser(request);
            return ResponseEntity.ok("Email verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestParam String email,
                                       String code) {

        boolean isValid = verificationCodeService.validateCode(email, code);
        if (isValid) {
            return ResponseEntity.ok("otp verification successes");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code");
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestParam String email
    ) {

        String result = service.sendOtpTOForgotPassword(email);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-passsowrd")
    public ResponseEntity<?> resetPassword(@Valid @RequestParam String email,
                                           @RequestParam String newPassword
    ) {

        String result = service.resetPassword(email, newPassword);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/login-json")
    public ResponseEntity<?> loginJson(@RequestBody LoginRequest loginRequest,
                                       HttpServletRequest request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );


            SecurityContextHolder.getContext().setAuthentication(authentication);


            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());


            User user = service.getCurrentUser();

            if (!Boolean.TRUE.equals(user.getVerificationStatus())) {
                SecurityContextHolder.clearContext();
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "success", false,
                                "error", "Account not verified"
                        ));
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "sessionId", session.getId(),
                    "user", Map.of(
                            "user_id", user.getId(),
                            "username", user.getUsername(),
                            "email", user.getEmail(),
                            "role", user.getRole(),
                            "fullName", user.getFirstName() + " " + user.getLastName()
                    )
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", "Invalid username or password"
                    ));
        }
    }


    @GetMapping("/client-profile-info")
    public ResponseEntity<?> getClientProfile(@RequestParam String userId) {
        try {
            ClientProfile clientProfile = service.getClientProfileInfo(userId);
            return ResponseEntity.ok(clientProfile);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ));
        }

    }

    @GetMapping("/service-provider-profile-info")
    public ResponseEntity<?> getServiceProviderProfile(@RequestParam String userId) {
        try {
            ServiceProviderProfile serviceProviderProfile = service.getServiceProviderProfileInfo(userId);
            return ResponseEntity.ok(serviceProviderProfile);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ));
        }

    }

    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return ResponseEntity.ok(Map.of(
                    "message", "No active session",
                    "authenticated", false
            ));
        }


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() &&
                !auth.getName().equals("anonymousUser");


        return ResponseEntity.ok(Map.of(
                "sessionId", session.getId(),
                "creationTime", session.getCreationTime(),
                "lastAccessTime", session.getLastAccessedTime(),
                "timeoutSeconds", session.getMaxInactiveInterval(),
                "authenticated", isAuthenticated,
                "username", isAuthenticated ? auth.getName() : null,
                "roles", isAuthenticated ? auth.getAuthorities() : null
        ));
    }

    @PutMapping("/{workerId}/add-new-skill")
    public ResponseEntity<?> addNewWorkerSkill(
            @PathVariable String workerId,
            @Valid @RequestBody WorkerSkillRequest workerSkillRequest
    ) {
        return ResponseEntity.ok(service.addWorkSkill(workerId, workerSkillRequest));

    }

    @DeleteMapping("/remove-new-skill")
    public ResponseEntity<?> removeNewSkill(
            @RequestParam String workerId,
            @RequestParam Integer skillId
    ) {
        return ResponseEntity.ok(service.removeSkillID(workerId, skillId));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute("SPRING_SECURITY_CONTEXT");


            session.invalidate();
        }

        SecurityContextHolder.clearContext();


        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }

    @GetMapping("/invalid-session")
    public ResponseEntity<?> invalidSession() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Invalid session",
                        "message", "Please login again"
                ));
    }

    @GetMapping("/logout-success")
    public ResponseEntity<?> logoutSuccess() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logged out successfully"
        ));
    }

    @DeleteMapping("/delete-my-account")
    public ResponseEntity<?> deleteMyAccount(
            @RequestParam String userId
    ) {

        return ResponseEntity.ok(service.deleteMyAccount(userId));
    }

}
