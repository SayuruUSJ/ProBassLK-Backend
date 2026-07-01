package lk.workbridge.marketplace.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lk.workbridge.marketplace.dto.LoginRequest;
import lk.workbridge.marketplace.dto.RegisterRequest;
import lk.workbridge.marketplace.dto.VerificationRequest;
import lk.workbridge.marketplace.entity.User;
import lk.workbridge.marketplace.service.AuthService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public String test(){
        return "Hello";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String result = service.register(request);
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

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "sessionId", session.getId(),
                    "user", Map.of(
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
}
