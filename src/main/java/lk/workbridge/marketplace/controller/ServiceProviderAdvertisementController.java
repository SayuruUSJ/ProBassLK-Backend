package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.ServiceProviderADResponse;
import lk.workbridge.marketplace.service.ServiceProviderAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-provider-advertisements")
@RequiredArgsConstructor
public class ServiceProviderAdvertisementController {

    private final ServiceProviderAdvertisementService serviceProviderAdvertisementService;
    @PostMapping
    public ResponseEntity<String> createAdvertisement(
            @RequestBody ServiceProviderAD serviceProviderAD) {

        String response = serviceProviderAdvertisementService.createNewAdvertisement(serviceProviderAD);
        return ResponseEntity.ok(response);
    }

    // ✅ 2. Update Advertisement Status
    @PutMapping("/{serviceId}/status")
    public ResponseEntity<Boolean> updateStatus(
            @PathVariable String serviceId,
            @RequestParam String status) {

        Boolean response = serviceProviderAdvertisementService
                .updateAdvertisementStatus(status, serviceId);

        return ResponseEntity.ok(response);
    }

    // ✅ 3. Get Advertisement by ID (with worker details if loaded)
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceProviderADResponse> getAdvertisement(
            @PathVariable String serviceId) {

        ServiceProviderADResponse response =
                serviceProviderAdvertisementService.getAdvertisementForSpecificWorker(serviceId);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-ads")
    public ResponseEntity<Page<ServiceProviderADResponse>> getAllAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                serviceProviderAdvertisementService.getAllAdvertisements(page, size)
        );
    }

}
