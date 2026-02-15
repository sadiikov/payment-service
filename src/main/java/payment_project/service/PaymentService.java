package payment_project.service;

import payment_project.dto.CreatePaymentRequest;
import payment_project.dto.PaymentResponse;
import payment_project.entity.Payment;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest);
    PaymentResponse getPaymentById(UUID paymentId);
}
