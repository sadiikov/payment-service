package payment_project.mapper;

import payment_project.dto.PaymentResponse;
import payment_project.entity.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getUserId(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }
}
