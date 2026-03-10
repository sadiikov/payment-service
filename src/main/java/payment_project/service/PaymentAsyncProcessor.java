package payment_project.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.entity.Wallet;
import payment_project.entity.enums.Status;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentSucceededEvent;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentAsyncProcessor {
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher publisher;

    @Async
    public void process(UUID paymentId) {
        for (int tries = 0; tries < 3; tries++){
            try {
                if(tries > 0){
                    log.warn("Retry payment {} attempt {}", paymentId, tries);
                }

                processInternal(paymentId);
                return;
            }catch (OptimisticLockException e){
                try {
                    Thread.sleep(100);
                }catch (InterruptedException ignored){}
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processInternal(UUID paymentId) {
        try {
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

            if (wallet.getBalance() < payment.getAmount()) {
                payment.setStatus(Status.FAILED);
                paymentRepository.save(payment);

                publisher.publishEvent(new PaymentFailedEvent(
                        payment.getId(), payment.getUserId(), payment.getAmount())
                );

                return;
            }

            try {
                wallet.setBalance(wallet.getBalance() - payment.getAmount());
                walletRepository.save(wallet);
            } catch (OptimisticLockException e) {
                payment.setStatus(Status.FAILED);
                paymentRepository.save(payment);
            }

            payment.setStatus(Status.SUCCESS);
            paymentRepository.save(payment);

            publisher.publishEvent(new PaymentSucceededEvent(
                    payment.getId(), payment.getUserId(), payment.getAmount()
            ));
        }catch (Exception e){
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

            payment.setStatus(Status.FAILED);
            paymentRepository.save(payment);

            throw e;
        }
    }
}
