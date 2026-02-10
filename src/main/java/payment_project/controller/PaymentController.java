package payment_project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment createPayment(@RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }
}
