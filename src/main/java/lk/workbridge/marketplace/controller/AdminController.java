package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.responses.ClientMoreInfo;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.dto.responses.UserBasicInfo;
import lk.workbridge.marketplace.dto.responses.WorkerMoreInfo;
import lk.workbridge.marketplace.service.AdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/get-all-users-base-info")
    public ResponseEntity<Page<UserBasicInfo>> getAllUsersBaseInfo(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllUsersBaseInfo(page, size));
    }

    @GetMapping("/get-worker-more-info")
    public ResponseEntity<WorkerMoreInfo> getWorkerMoreInfo(@RequestParam String workerId) {
        return ResponseEntity.ok(adminService.getWorkerMoreInfo(workerId));
    }

    @GetMapping("/get-client-more-info")
    public ResponseEntity<ClientMoreInfo> getClientMoreInfo(@RequestParam String clientId) {
        return ResponseEntity.ok(adminService.getClientMoreInfo(clientId));
    }

    @PutMapping("/service-provider-advertisement/status")
    public ResponseEntity<Map<String, String>> acceptOrRejectServiceProviderAdvertisement(
            @RequestParam String serviceId,
            @RequestParam String status) {

        boolean updated = adminService.acceptOrRejectServiceProviderAdvertisement(serviceId, status);

        Map<String, String> response = new HashMap<>();
        if (updated) {
            response.put("message", "Advertisement " + status + " successfully");
            response.put("serviceId", serviceId);
            response.put("status", status);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update advertisement status");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Accept or reject a service wanted advertisement
     */
    @PutMapping("/service-wanted-advertisement/status")
    public ResponseEntity<Map<String, String>> acceptOrRejectServiceWantedAdvertisement(
            @RequestParam String advertisementId,
            @RequestParam String status) {

        boolean updated = adminService.acceptOrRejectServiceWantedAdvertisement(advertisementId, status);

        Map<String, String> response = new HashMap<>();
        if (updated) {
            response.put("message", "Advertisement " + status + " successfully");
            response.put("advertisementId", advertisementId);
            response.put("status", status);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update advertisement status");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Get all pending service provider advertisements with pagination
     */
    @GetMapping("/pending-service-provider-advertisements")
    public ResponseEntity<Page<ServiceProviderADResponse>> getAllPendingServiceProviderAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllPendingServiceProviderAdvertisements(page, size));
    }

    /**
     * Get all pending service wanted advertisements with pagination
     */
    @GetMapping("/pending-wanted-advertisements")
    public ResponseEntity<Page<ServiceWantedADResponse>> getAllPendingWantedAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllPendingWantedAdvertisements(page, size));
    }

    /**
     * Delete a service provider advertisement
     */
    @DeleteMapping("/service-provider-advertisement/{serviceId}")
    public ResponseEntity<Map<String, String>> deleteServiceProviderAdvertisement(
            @PathVariable String serviceId) {

        String result = adminService.deleteServiceProviderAdvertisement(serviceId);

        Map<String, String> response = new HashMap<>();
        if (result != null && result.equals("Deleted successfully")) {
            response.put("message", result);
            response.put("serviceId", serviceId);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", result != null ? result : "Failed to delete advertisement");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Delete a service wanted advertisement
     */
    @DeleteMapping("/service-wanted-advertisement/{advertisementId}")
    public ResponseEntity<Map<String, String>> deleteServiceWantedAdvertisement(
            @PathVariable String advertisementId) {

        String result = adminService.deleteServiceWantedAdvertisement(advertisementId);

        Map<String, String> response = new HashMap<>();
        if (result != null && result.equals("Deleted successfully")) {
            response.put("message", result);
            response.put("advertisementId", advertisementId);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", result != null ? result : "Failed to delete advertisement");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Delete a user by ID
     */
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId) {

        String result = adminService.deleteUser(userId);

        Map<String, String> response = new HashMap<>();
        if (result != null && result.equals("Deleted successfully")) {
            response.put("message", result);
            response.put("userId", userId);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", result != null ? result : "Failed to delete user");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("/get-all-service-provider-advertisments")
    public ResponseEntity<Page<ServiceProviderADResponse>> getAllServiceProviderAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                adminService.getAllServiceProviderAdvertisements(page, size)
        );
    }
    @GetMapping("/get-all-service-wanted-advertisments")
    public ResponseEntity<Page<ServiceWantedADResponse>> getAllServiceWantedAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                adminService.getAllServiceWantedAdvertisements(page, size)
        );
    }
}
