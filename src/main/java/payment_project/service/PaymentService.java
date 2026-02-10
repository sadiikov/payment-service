package payment_project.service;

import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Payment;

public interface PaymentService {
    public Payment createPayment(CreatePaymentRequest createPaymentRequest);
}
