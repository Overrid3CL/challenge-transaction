package alvarado.ms.transaction.exception;

import alvarado.ms.transaction.exception.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    private static final String REQUEST_PATH = "/transaction/1";

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn(REQUEST_PATH);
    }

    @Test
    void handleTransactionNotFoundException() {
        TransactionNotFoundException ex = new TransactionNotFoundException(999);

        ResponseEntity<ErrorResponseDTO> response = handler.handleTransactionNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Transaction with ID 999 not found", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
        assertNull(body.getValidationErrors());
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");

        ResponseEntity<ErrorResponseDTO> response = handler.handleEntityNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("Entity not found", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleEntityNotFoundException_NoSuchElement() {
        NoSuchElementException ex = new NoSuchElementException("No value present");

        ResponseEntity<ErrorResponseDTO> response = handler.handleEntityNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals("No value present", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleBadRequestException_BusinessRuleViolation() {
        BusinessRuleViolationException ex = new BusinessRuleViolationException("Business rule violated");

        ResponseEntity<ErrorResponseDTO> response = handler.handleBadRequestException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("Business rule violated", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleBadRequestException_InvalidTransaction() {
        InvalidTransactionException ex = new InvalidTransactionException("Invalid transaction");

        ResponseEntity<ErrorResponseDTO> response = handler.handleBadRequestException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("Invalid transaction", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleBadRequestException_IllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponseDTO> response = handler.handleBadRequestException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("Invalid argument", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleValidationException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "userId", "User ID is required"));
        bindingResult.addError(new FieldError("target", "amount", "Amount must be positive"));
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidationException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Validation Failed", body.getError());
        assertEquals("Validation errors occurred", body.getMessage());
        assertEquals(REQUEST_PATH, body.getPath());
        assertNotNull(body.getValidationErrors());
        assertEquals(2, body.getValidationErrors().size());
        assertEquals("User ID is required", body.getValidationErrors().get("userId"));
        assertEquals("Amount must be positive", body.getValidationErrors().get("amount"));
    }

    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate key");

        ResponseEntity<ErrorResponseDTO> response = handler.handleDataIntegrityViolationException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(409, body.getStatus());
        assertEquals("Conflict", body.getError());
        assertTrue(body.getMessage().startsWith("Data integrity violation: "));
        assertTrue(body.getMessage().contains("Duplicate key"));
        assertEquals(REQUEST_PATH, body.getPath());
    }

    @Test
    void handleGenericException() {
        Exception ex = new Exception("Unexpected error");

        ResponseEntity<ErrorResponseDTO> response = handler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDTO body = response.getBody();
        assertEquals(500, body.getStatus());
        assertEquals("Internal Server Error", body.getError());
        assertTrue(body.getMessage().startsWith("An unexpected error occurred: "));
        assertTrue(body.getMessage().contains("Unexpected error"));
        assertEquals(REQUEST_PATH, body.getPath());
    }
}
