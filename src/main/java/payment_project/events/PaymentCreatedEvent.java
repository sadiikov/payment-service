package payment_project.events;

import java.util.UUID;

public record PaymentCreatedEvent(UUID paymentId, Long userId, Long amount) { }