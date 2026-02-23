package payment_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Wallet {
    @Id
    private Long userId;

    private Long balance;

    @Version
    private Integer version;
}
