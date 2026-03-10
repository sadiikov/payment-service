package payment_project.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import payment_project.events.PaymentCreatedEvent;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentRefundedEvent;
import payment_project.events.PaymentSucceededEvent;

@Component
public class PaymentEventListener {
    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @EventListener
    public void handlePaymentCreated(PaymentCreatedEvent event) {
        log.info("Payment created {}, {}, {}", event.paymentId(), event.userId(), event.amount());
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
