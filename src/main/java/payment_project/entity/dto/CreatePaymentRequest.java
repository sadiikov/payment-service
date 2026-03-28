package payment_project.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequest(
        @NotNull
        Long userId,

        @NotNull
        @Positive
        Long amount
) {
}