package payment_project.entity;

import payment_project.entity.enums.Status;

import java.sql.Timestamp;
import java.util.UUID;

public class Payment {
    private UUID idempotencyKey;
    private Long userId;
    private Long amount;
    private Status status;
    private Timestamp createdAt;
}
