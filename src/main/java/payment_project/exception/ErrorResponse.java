package payment_project.exception;

public record ErrorResponse(
        String message,
        int status,
        String timestamp
) {}