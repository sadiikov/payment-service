package payment_project.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import payment_project.entity.Payment;
import payment_project.events.PaymentEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCreated(Payment payment) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.created",
                new PaymentEvent(
                        payment.getId(),
                        payment.getUserId(),
                        payment.getAmount(),
                        "CREATED"
                )
        );
    }

    public void publishSuccess(Payment payment) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.success",
                new PaymentEvent(
                        payment.getId(),
                        payment.getUserId(),
                        payment.getAmount(),
                        "SUCCESS"
                )
        );
    }

    public void publishFailed(Payment payment) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.failed",
                new PaymentEvent(
                        payment.getId(),
                        payment.getUserId(),
                        payment.getAmount(),
                        "FAILED"
                )
        );
    }

    public void publishRefunded(UUID id, Long userId, Long amount) {
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.refunded",
                new PaymentEvent(id, userId, amount, "REFUNDED")
        );
    }
}
