package payment_project.events;

import java.util.UUID;

public record PaymentFailedEvent(UUID paymentId, Long userId, Long amount) { }