package payment_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import payment_project.entity.enums.Status;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
public class Payment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID idempotencyKey;

    private Long userId;
    private Long amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Instant createdAt;
}

