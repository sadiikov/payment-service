package payment_project.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import payment_project.base.AbstractIntegrationTest;
import payment_project.entity.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.entity.Wallet;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;
import payment_project.service.PaymentService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentIdempotencyIT extends AbstractIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void sameRequestThreeTimes_shouldProcessOnlyOnce() throws Exception {

        UUID key = UUID.fromString("11111111-1111-1111-1111-111111111113");

        paymentService.createPayment(new CreatePaymentRequest(2L, 50L, key));
        paymentService.createPayment(new CreatePaymentRequest(2L, 50L, key));
        paymentService.createPayment(new CreatePaymentRequest(2L, 50L, key));

        Thread.sleep(1500);

        List<Payment> payments = paymentRepository.findAll();
        Wallet wallet = walletRepository.findById(2L).orElseThrow();

        assertEquals(1, payments.size());
        assertEquals(0, wallet.getBalance());
    }
}

