package lk.workbridge.marketplace.controller;

import jakarta.validation.Valid;
import lk.workbridge.marketplace.dto.ClientBookingRequestAD;
import lk.workbridge.marketplace.service.HireRequestService;
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
public class HireRequestController {

    private final HireRequestService hireRequestService;

    @PostMapping("/create-advertisement")
    public ResponseEntity<?> register(@Valid @RequestBody ClientBookingRequestAD request) {
        String result = hireRequestService.requestAdvertisement(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> acceptOrReject(@RequestParam String advertisementId,
                                            @RequestParam String status) {
        Boolean result = hireRequestService.acceptOrReject(advertisementId, status);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-completed-jobs")
    public ResponseEntity<?> updateCompletedJobs(@RequestParam String advertisementId,
                                                  @RequestParam String status) {
        boolean isCompleted = hireRequestService.updateCompleteOrIncompleteJobs(advertisementId, status);
        return ResponseEntity.ok(isCompleted);
    }

    @GetMapping("/get-requests")
    public ResponseEntity<?> getAllRequestsByWorkerId(@RequestParam String workerId) {
        return ResponseEntity.ok(hireRequestService.getAllRequestsByWorkerId(workerId));
    }

    @PostMapping("/cancel-request")
    public ResponseEntity<?> cancelRequest(
            @RequestParam String hireAdvertisementId
    ){
        return ResponseEntity.ok(hireRequestService.cancelRequest(hireAdvertisementId));
    }

}
