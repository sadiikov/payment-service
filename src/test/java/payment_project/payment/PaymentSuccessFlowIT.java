package payment_project.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import payment_project.base.AbstractIntegrationTest;
import payment_project.entity.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.entity.Wallet;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;
import payment_project.service.PaymentService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentSuccessFlowIT extends AbstractIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldDecreaseWalletAndMarkPaymentSuccess() throws Exception {

        UUID key = UUID.fromString("11111111-1111-1111-1111-111111111112");

        paymentService.createPayment(new CreatePaymentRequest(1L, 100L, key));

        Thread.sleep(1000); // ждём async

        Payment payment = paymentRepository.findByIdempotencyKey(key).orElseThrow();
        Wallet wallet = walletRepository.findById(1L).orElseThrow();

        assertEquals(Status.SUCCESS, payment.getStatus());
        assertEquals(900, wallet.getBalance());
    }
}
