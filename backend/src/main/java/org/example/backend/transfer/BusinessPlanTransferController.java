package org.example.backend.transfer;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BusinessPlanTransferController {

    private final BusinessPlanTransferService transferService;

    public BusinessPlanTransferController(BusinessPlanTransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer-to-business-plan")
    public ResponseEntity<BusinessPlanTransferResponse> transferToBusinessPlan(
            @Valid @RequestBody BusinessPlanTransferRequest request) {
        return ResponseEntity.ok(transferService.createRedirect(request));
    }
}