package payment_project.dto;

import java.util.UUID;

public record CreatePaymentRequest(
        Long userId,
        Long amount,
        UUID idempotencyKey
) { }