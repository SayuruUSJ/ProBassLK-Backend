package lk.workbridge.marketplace.controller;

import lk.workbridge.marketplace.dto.responses.ClientProfileCounts;
import lk.workbridge.marketplace.service.ClientAndServiceProviderProfileCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/profile-counts")
@RequiredArgsConstructor
public class ProfileCountController {
    private final ClientAndServiceProviderProfileCountService clientAndServiceProviderProfileCountService;

    @GetMapping("/get-client-counts")
    public ResponseEntity<ClientProfileCounts> getClientProfileCounts(
            @RequestParam String clientId
    ) {
        return ResponseEntity.ok(clientAndServiceProviderProfileCountService.getCounts(clientId));

    }
}
