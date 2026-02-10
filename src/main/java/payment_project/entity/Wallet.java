package payment_project.entity;

import jakarta.persistence.*;

@Entity
public class Wallet {
    @Id
    private Long userId;

    private Long balance;

    @Version
    private Integer version;
}
