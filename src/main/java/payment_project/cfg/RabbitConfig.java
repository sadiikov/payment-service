package payment_project.cfg;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "payment.exchange";

    public static final String CREATED_QUEUE = "payment.created.queue";
    public static final String SUCCESS_QUEUE = "payment.success.queue";
    public static final String FAILED_QUEUE = "payment.failed.queue";
    public static final String REFUNDED_QUEUE = "payment.refunded.queue";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(CREATED_QUEUE);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder.bind(createdQueue())
                .to(exchange())
                .with("payment.created");
    }

    @Bean
    public Queue successQueue() {
        return new Queue(SUCCESS_QUEUE);
    }

    @Bean
    public Binding successBinding() {
        return BindingBuilder.bind(successQueue())
                .to(exchange())
                .with("payment.success");
    }

    @Bean
    public Queue failedQueue() {
        return new Queue(FAILED_QUEUE);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder.bind(failedQueue())
                .to(exchange())
                .with("payment.failed");
    }

    @Bean
    public Queue refundedQueue() {
        return new Queue(REFUNDED_QUEUE);
    }

    @Bean
    public Binding refundedBinding() {
        return BindingBuilder
                .bind(refundedQueue())
                .to(exchange())
                .with("payment.refunded");
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
