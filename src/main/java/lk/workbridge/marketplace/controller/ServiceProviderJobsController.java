package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.service.ServiceProviderJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/service-provider-jobs")
public class ServiceProviderJobsController {
    private  final ServiceProviderJobService serviceProviderJobService;

    @GetMapping("/get-ongoing-jobs")
    public ResponseEntity<?> getAllOngoingJobsServiceProvider(
            @RequestParam String serviceProviderId

    ){
        return ResponseEntity.ok(serviceProviderJobService.getServiceProviderOngoingJobs(serviceProviderId));

    }

    @GetMapping("/get-compelted-jobs")
    public ResponseEntity<?> getAllCompletedJobsServiceProvider(
            @RequestParam String serviceProviderId

    ){
        return ResponseEntity.ok(serviceProviderJobService.getServiceProviderCompletedJobs(serviceProviderId));

    }

    @GetMapping("/get-cancelled-jobs")
    public ResponseEntity<?> getAllCancelledJobsServiceProvider(
            @RequestParam String serviceProviderId

    ){
        return ResponseEntity.ok(serviceProviderJobService.getServiceProviderCancelledJobs(serviceProviderId));

    }

    @PutMapping("/update-service-provider-availabilty-status")
    public ResponseEntity<?> handleServiceProviderAndAdvertisementAvailability(
            @RequestParam String serviceProviderId,
            @RequestParam String hireRequestId
    ){
        return  ResponseEntity.ok(serviceProviderJobService.handleServiceProviderAndAdvertisementAvailability(serviceProviderId,hireRequestId));
    }

    @PutMapping("/update-service-provider-availabilty-wanted-ad-status")
    public ResponseEntity<?> handleServiceWantedAdvertisementAvailability(
            @RequestParam int requestId,
            @RequestParam String serviceProviderId
    ){

        return  ResponseEntity.ok(serviceProviderJobService.handleServiceWantedAdvertisement(requestId,serviceProviderId));
    }
}
