package payment_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import payment_project.entity.enums.Status;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "idempotency_key")
        }
)
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private UUID idempotencyKey;

    private Long userId;
    private Long amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant createdAt;
}

