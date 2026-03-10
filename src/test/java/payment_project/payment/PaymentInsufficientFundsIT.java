package payment_project.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import payment_project.base.AbstractIntegrationTest;
import payment_project.entity.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;
import payment_project.service.PaymentService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentInsufficientFundsIT extends AbstractIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldFailWhenNotEnoughMoney() throws Exception {

        UUID key = UUID.fromString("11111111-1111-1111-1111-111111111113");

        paymentService.createPayment(new CreatePaymentRequest(2L, 100L, key));

        Thread.sleep(1000);

        Payment payment = paymentRepository.findByIdempotencyKey(key).orElseThrow();

        assertEquals(Status.FAILED, payment.getStatus());
    }
}
