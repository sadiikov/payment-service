package payment_project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import payment_project.dto.CreatePaymentRequest;
import payment_project.entity.Payment;
import payment_project.events.PaymentCreatedEvent;
import payment_project.repository.PaymentRepository;
import payment_project.service.PaymentServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @InjectMocks
    PaymentServiceImpl service;

    @Test
    void shouldCreatePayment() {
        CreatePaymentRequest request = new CreatePaymentRequest(1L, 100L);

        service.createPayment(request);

        verify(paymentRepository).save(any(Payment.class));
        verify(publisher).publishEvent(any(PaymentCreatedEvent.class));
    }
}