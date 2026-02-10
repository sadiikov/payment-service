package payment_project.service;

import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Payment;

import java.util.UUID;

public interface PaymentService {
    Payment createPayment(CreatePaymentRequest createPaymentRequest);
    Payment getPaymentById(UUID paymentId);
}
