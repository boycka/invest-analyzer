package org.example.backend.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.payment.dto.PaymentConfirmRequest;
import org.example.backend.payment.dto.PaymentConfirmResponse;
import org.example.backend.payment.dto.PaymentCreateResponse;
import org.example.backend.payment.service.PayPalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/analyse", "/api/analysis"})
public class PaymentController {

    private final PayPalService payPalService;

    @PostMapping("/paiement")
    public ResponseEntity<PaymentCreateResponse> create(@Valid @RequestBody AnalysisRequest request) {
        return ResponseEntity.ok(payPalService.createOrder(request));
    }

    @PostMapping("/paiement/confirmer")
    public ResponseEntity<PaymentConfirmResponse> confirm(@Valid @RequestBody PaymentConfirmRequest request) {
        return ResponseEntity.ok(payPalService.capture(
                new PayPalService.PaymentConfirmRequestData(request.orderId(), request.analysis())
        ));
    }
}