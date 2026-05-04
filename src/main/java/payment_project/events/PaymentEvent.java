package payment_project.events;

import java.util.UUID;

public record PaymentEvent(
        UUID paymentId,
        Long userId,
        Long amount,
        String type
) {}
