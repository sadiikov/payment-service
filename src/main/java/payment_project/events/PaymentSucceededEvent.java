package payment_project.events;

import java.util.UUID;

public record PaymentSucceededEvent(UUID paymentId, Long userId, Long amount) { }