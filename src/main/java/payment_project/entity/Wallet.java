package payment_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
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
