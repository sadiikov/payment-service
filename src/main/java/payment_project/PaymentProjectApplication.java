package payment_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PaymentProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentProjectApplication.class, args);
    }

}
