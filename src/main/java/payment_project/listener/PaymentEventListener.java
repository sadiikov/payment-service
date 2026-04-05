package payment_project.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import payment_project.events.PaymentCreatedEvent;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentRefundedEvent;
import payment_project.events.PaymentSucceededEvent;
import payment_project.service.PaymentAsyncProcessor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentAsyncProcessor asyncProcessor;
    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCreated(PaymentCreatedEvent event) {
        log.info("Payment created {}, {}, {}", event.paymentId(), event.userId(), event.amount());

        asyncProcessor.process(event.paymentId());
    }

    @EventListener
    public void handlePaymentSuccess(PaymentSucceededEvent event) {
        log.info("Payment successful {}, {}, {}", event.paymentId(), event.userId(), event.amount());

    }

    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.error("Payment failed {}, {}, {}", event.paymentId(), event.userId(), event.amount());
    }

    @EventListener
    public void handlePaymentRefunded(PaymentRefundedEvent event) {
        log.info("Payment refunded {}, {}, {}", event.paymentId(), event.userId(), event.amount());
    }
}
