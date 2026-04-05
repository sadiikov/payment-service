package payment_project.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.dto.CreatePaymentRequest;
import payment_project.dto.PaymentInfo;
import payment_project.dto.PaymentResponse;
import payment_project.entity.enums.Status;
import payment_project.events.PaymentCreatedEvent;
import payment_project.events.PaymentRefundedEvent;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        Payment payment = new Payment();
        payment.setUserId(request.userId());
        payment.setAmount(request.amount());
        payment.setStatus(Status.NEW);
        payment.setCreatedAt(Instant.now());

        paymentRepository.save(payment);

        publisher.publishEvent(
                new PaymentCreatedEvent(payment.getId(), payment.getUserId(), payment.getAmount())
        );

        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }

    @Transactional
    @Override
    public void refundPayment(UUID paymentId) {
        Status status = paymentRepository.getStatus(paymentId);

        if (status != Status.SUCCESS) {
            throw new IllegalArgumentException("Only successful payment can be refunded");
        }

        PaymentInfo info = paymentRepository.getPaymentInfo(paymentId);

        walletRepository.deposit(info.userId(), info.amount());

        paymentRepository.updateStatus(paymentId, Status.REFUNDED);

        publisher.publishEvent(new PaymentRefundedEvent(
                paymentId, info.userId(), info.amount())
        );
    }
}