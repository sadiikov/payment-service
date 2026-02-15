package payment_project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import payment_project.dto.CreatePaymentRequest;
import payment_project.dto.PaymentResponse;
import payment_project.entity.Payment;
import payment_project.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        return ResponseEntity.accepted().body(paymentService.createPayment(request));
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id)).getBody();
    }
}
