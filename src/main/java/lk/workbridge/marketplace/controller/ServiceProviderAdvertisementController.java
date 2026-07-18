package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.ServiceProviderAD;
import lk.workbridge.marketplace.dto.responses.ServiceProviderADResponse;
import lk.workbridge.marketplace.service.ServiceProviderAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-provider-advertisements")
@RequiredArgsConstructor
public class ServiceProviderAdvertisementController {

    private final ServiceProviderAdvertisementService serviceProviderAdvertisementService;
    @PostMapping("/create-advertisement")
    public ResponseEntity<String> createAdvertisement(
            @RequestBody ServiceProviderAD serviceProviderAD) {

        String response = serviceProviderAdvertisementService.createNewAdvertisement(serviceProviderAD);
        return ResponseEntity.ok(response);
    }


//    @PutMapping("/update-status")
//    public ResponseEntity<Boolean> updateStatus(
//            @RequestParam String serviceId,
//            @RequestParam String status) {
//
//        Boolean response = serviceProviderAdvertisementService
//                .updateAdvertisementStatus(status, serviceId);
//
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/{workerId}")
    public ResponseEntity<ServiceProviderADResponse> getAdvertisement(
            @PathVariable String workerId) {

        ServiceProviderADResponse response =
                serviceProviderAdvertisementService.getAdvertisementForSpecificWorker(workerId);

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

//    @GetMapping("/get-all-pending-ads")
//    public ResponseEntity<Page<ServiceProviderADResponse>> getAllPendingAdvertisements(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        return ResponseEntity.ok(
//                serviceProviderAdvertisementService.getAllPendingAdvertisements(page, size)
//        );
//    }

}
