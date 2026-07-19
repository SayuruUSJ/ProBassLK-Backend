package lk.workbridge.marketplace.controller;

import jakarta.validation.Valid;
import lk.workbridge.marketplace.dto.ServiceProviderRequestForWantedAD;
import lk.workbridge.marketplace.dto.ServiceWantedAD;
import lk.workbridge.marketplace.dto.responses.ServiceWantedADResponse;
import lk.workbridge.marketplace.dto.responses.WantedAdvertisementApplication;
import lk.workbridge.marketplace.service.ApplicationForWantedADService;
import lk.workbridge.marketplace.service.ServiceWantedAdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/service-wanted-advertisements")
@RequiredArgsConstructor
public class ServiceWantedAdvertisementController {
    private final ServiceWantedAdvertisementService serviceWantedAdvertisementService;
    private final ApplicationForWantedADService applicationForWantedADService;

    @PostMapping("/create-advertisement")
    public ResponseEntity<?> register(@Valid @RequestBody ServiceWantedAD request) {
        String result = serviceWantedAdvertisementService.requestAdvertisement(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create-request-for-wanted-advertisement")
    public ResponseEntity<?> createNewRequestForAdFromServiceProvider(
            @Valid @RequestBody ServiceProviderRequestForWantedAD request) {

        try {

            String response = applicationForWantedADService.createNewRequest(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong.");

        }
    }

    @PutMapping("/update-request")
    public ResponseEntity<?> updateRequestedAd(@RequestParam String ad_status,
                                               @RequestParam int request_id,
                                               @RequestParam String request_status
    ) {

        try {

            String response = applicationForWantedADService.updateRequest(ad_status, request_id, request_status);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong.");

        }

    }

    @GetMapping("/get-all-wanted-ads")
    public ResponseEntity<Page<ServiceWantedADResponse>> getAllAdvertisements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                serviceWantedAdvertisementService.getAllAdvertisements(page, size)
        );
    }

    @GetMapping("/get-client-all-wanted-ads")
    public ResponseEntity<Page<ServiceWantedADResponse>> getAllAdvertisementByClientId(
            @RequestParam String clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                serviceWantedAdvertisementService.getClientSpecificAdvertisements(clientId, page, size)
        );

    }

    @GetMapping("/get-all-applications-clientwise")
    public  ResponseEntity<Page<WantedAdvertisementApplication>> getClientApplications(
            @RequestParam String clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(applicationForWantedADService.getClientApplications(clientId,page,size));


    }

}
