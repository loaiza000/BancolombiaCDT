package finalCDT.finalCDT.exceptions;

import finalCDT.finalCDT.helpers.ApiResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger;

    public GlobalExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<Object>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        logger.error("User already exists: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<Object>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.error("Validation error: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<Map<String, String>>(false, "Validation failed", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.error("Constraint violation: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<Map<String, String>>(false, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        logger.error("Unexpected error occurred: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<Object>(false, "An unexpected error occurred", null));
    }
}
