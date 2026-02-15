package payment_project.dto;

import payment_project.entity.enums.Status;

import java.time.Instant;

public record PaymentResponse(
        Long amount,
        Status status,
        Instant createdAt
) {}
