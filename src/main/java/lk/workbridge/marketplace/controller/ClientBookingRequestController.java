package lk.workbridge.marketplace.controller;

import jakarta.validation.Valid;
import lk.workbridge.marketplace.dto.ClientBookingRequestAD;
import lk.workbridge.marketplace.service.ClientBookingRequestAdvertisementService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/client-requests")
@RequiredArgsConstructor
public class ClientBookingRequestController {

    private final ClientBookingRequestAdvertisementService clientRequestAdvertisementService;

    @PostMapping("/create-advertisement")
    public ResponseEntity<?> register(@Valid @RequestBody ClientBookingRequestAD request) {
        String result = clientRequestAdvertisementService.requestAdvertisement(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> acceptOrReject(@RequestParam String advertisementId,
                                            @RequestParam String status) {
        Boolean result = clientRequestAdvertisementService.acceptOrReject(advertisementId, status);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-completed-jobs")
    public ResponseEntity<?> updateCompletedJobs(@RequestParam String advertisementId,
                                                  @RequestParam String status) {
        boolean isCompleted = clientRequestAdvertisementService.updateCompleteOrIncompleteJobs(advertisementId, status);
        return ResponseEntity.ok(isCompleted);
    }

    @GetMapping("/get-requests")
    public ResponseEntity<?> getAllRequestsByWorkerId(@RequestParam String workerId) {
        return ResponseEntity.ok(clientRequestAdvertisementService.getAllRequestsByWorkerId(workerId));
    }

}
