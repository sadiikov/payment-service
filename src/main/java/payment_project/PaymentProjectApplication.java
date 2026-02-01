package payment_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;

@SpringBootApplication
public class PaymentProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentProjectApplication.class, args);
        System.out.println(Instant.now());
    }

}
