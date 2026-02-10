package payment_project.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable UUID id) {
        return paymentService.getPaymentById(id);
    }
}
