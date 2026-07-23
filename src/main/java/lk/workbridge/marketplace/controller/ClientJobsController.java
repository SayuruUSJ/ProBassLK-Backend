package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.service.ClientJobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/client-jobs")
@RequiredArgsConstructor
public class ClientJobsController {
    private  final ClientJobsService clientJobsService;

    @GetMapping("/get-ongoing-jobs")
    public ResponseEntity<?> getAllOngoingJobsClient(
            @RequestParam String clientId

    ){
        return ResponseEntity.ok(clientJobsService.getClientOngoingJobs(clientId));

    }

    @GetMapping("/get-compelted-jobs")
    public ResponseEntity<?> getAllCompletedJobsClient(
            @RequestParam String clientId

    ){
        return ResponseEntity.ok(clientJobsService.getClientCompletedJobs(clientId));

    }

    @GetMapping("/get-cancelled-jobs")
    public ResponseEntity<?> getAllCancelledJobsClient(
            @RequestParam String clientId

    ){
        return ResponseEntity.ok(clientJobsService.getClientCancelledJobs(clientId));

    }

}
