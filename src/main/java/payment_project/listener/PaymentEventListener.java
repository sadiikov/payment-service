package payment_project.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import payment_project.events.PaymentCreatedEvent;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentRefundedEvent;
import payment_project.events.PaymentSucceededEvent;

@Component
public class PaymentEventListener {
    @EventListener
    public void handlePaymentCreated(PaymentCreatedEvent event) {
        System.out.println("Payment created event: " + event.paymentId());
    }

    @EventListener
    public void handlePaymentSuccess(PaymentSucceededEvent event) {
        System.out.println("Payment success event: " + event.paymentId());
    }

    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        System.out.println("Payment failed event: " + event.paymentId());
    }

    @EventListener
    public void handlePaymentRefunded(PaymentRefundedEvent event) {
        System.out.println("Payment refunded event: " + event.paymentId());
    }
}
