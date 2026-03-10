package payment_project.events;

import java.util.UUID;

public record PaymentRefundedEvent(UUID paymentId, Long userId, Long amount) { }