package payment_project.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import payment_project.base.AbstractIntegrationTest;
import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Wallet;
import payment_project.repository.WalletRepository;
import payment_project.service.PaymentService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrentPaymentIT extends AbstractIntegrationTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void concurrentPayments_shouldNotGoNegative() throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for(int i = 0; i < 5; i++) {
            executor.submit(() ->
                    paymentService.createPayment(
                            new CreatePaymentRequest(2L, 50L, UUID.randomUUID()))
            );
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        Thread.sleep(1500);

        Wallet wallet = walletRepository.findById(2L).orElseThrow();

        assertTrue(wallet.getBalance() >= 0);
    }
}
