package payment_project.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import payment_project.cfg.RabbitConfig;
import payment_project.events.PaymentEvent;
import payment_project.service.PaymentAsyncProcessor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentAsyncProcessor asyncProcessor;
    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @RabbitListener(queues = RabbitConfig.CREATED_QUEUE)
    public void handlePaymentCreated(PaymentEvent event) {
        log.info("Payment created {}, {}, {}", event.paymentId(), event.userId(), event.amount());

        asyncProcessor.process(event.paymentId());
    }

    @RabbitListener(queues = RabbitConfig.SUCCESS_QUEUE)
    public void handlePaymentSuccess(PaymentEvent event) {
        log.info("Payment successful {}, {}, {}", event.paymentId(), event.userId(), event.amount());

    }

    @RabbitListener(queues = RabbitConfig.FAILED_QUEUE)
    public void handlePaymentFailed(PaymentEvent event) {
        log.error("Payment failed {}, {}, {}", event.paymentId(), event.userId(), event.amount());
    }

    @RabbitListener(queues = RabbitConfig.REFUNDED_QUEUE)
    public void handlePaymentRefunded(PaymentEvent event) {
        log.info("Payment refunded {}, {}, {}", event.paymentId(), event.userId(), event.amount());
    }
}
