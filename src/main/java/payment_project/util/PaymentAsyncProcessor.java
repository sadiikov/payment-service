package payment_project.util;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import payment_project.entity.Payment;
import payment_project.entity.Wallet;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentAsyncProcessor {
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;

    @Async
    public void process(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        log.info("Payment userId = {}, amount = {}, status = {}"
                , payment.getUserId(), payment.getAmount(), payment.getStatus());

        payment.setStatus(Status.PENDING);
        paymentRepository.save(payment);

        Wallet wallet = walletRepository.findById(payment.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        log.info("Wallet userId = {}, balance = {}, version = {}"
                , wallet.getUserId(), wallet.getBalance(), wallet.getVersion());

        if(wallet.getBalance() < payment.getAmount()) {
            payment.setStatus(Status.FAILED);
            paymentRepository.save(payment);
            return;
        }

        wallet.setBalance(wallet.getBalance() - payment.getAmount());
        walletRepository.save(wallet);

        payment.setStatus(Status.SUCCESS);
        paymentRepository.save(payment);
    }
}
