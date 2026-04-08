package payment_project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentSucceededEvent;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;
import payment_project.service.PaymentAsyncProcessor;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentAsyncProcessorTest {

    @Mock
    PaymentRepository paymentRepository;
    @Mock
    WalletRepository walletRepository;
    @Mock
    ApplicationEventPublisher publisher;

    @InjectMocks
    PaymentAsyncProcessor processor;

    @Test
    void shouldProcessSuccessfully() {
        UUID id = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setId(id);
        payment.setUserId(1L);
        payment.setAmount(100L);

        when(paymentRepository.findById(id))
                .thenReturn(Optional.of(payment));

        when(walletRepository.withdraw(1L, 100L))
                .thenReturn(1);

        processor.process(id);

        verify(paymentRepository).updateStatus(id, Status.PENDING);
        verify(paymentRepository).updateStatus(id, Status.SUCCESS);
        verify(publisher).publishEvent(any(PaymentSucceededEvent.class));
    }

    @Test
    void shouldFailWhenNotEnoughBalance() {
        UUID id = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setId(id);
        payment.setUserId(1L);
        payment.setAmount(100L);

        when(paymentRepository.findById(id))
                .thenReturn(Optional.of(payment));

        when(walletRepository.withdraw(1L, 100L))
                .thenReturn(0);

        processor.process(id);

        verify(paymentRepository).updateStatus(id, Status.PENDING);
        verify(paymentRepository).updateStatus(id, Status.FAILED);
        verify(publisher).publishEvent(any(PaymentFailedEvent.class));
    }
}