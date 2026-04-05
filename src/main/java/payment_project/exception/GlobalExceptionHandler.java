package payment_project.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        log.error("EntityNotFoundException", ex);

        return ResponseEntity.status(404).body(
                new ErrorResponse(ex.getMessage(), 404, Instant.now().toString())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
        log.error("IllegalArgumentException", ex);

        return ResponseEntity.badRequest().body(
                new ErrorResponse(ex.getMessage(), 400, Instant.now().toString())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDb(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException", ex);

        return ResponseEntity.status(409).body(
                new ErrorResponse(ex.getMessage(), 409, Instant.now().toString())
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<?> handleBalance(InsufficientBalanceException ex) {
        log.error("InsufficientBalanceException", ex);

        return ResponseEntity.status(409).body(
                new ErrorResponse(ex.getMessage(), 409, Instant.now().toString())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Exception", ex);

        return ResponseEntity.status(500).body(
                new ErrorResponse(ex.getMessage(), 500, Instant.now().toString())
        );
    }
}
