package com.example.demo.services.customer.web.handler;

import com.example.demo.services.customer.TestLifecycleLogger;
import com.example.demo.services.customer.accounts.exception.AccountIbanFormatException;
import com.example.demo.services.customer.accounts.exception.WebclientConnectionError;
import com.example.demo.services.customer.addresses.exception.AddressNotFoundException;
import com.example.demo.services.customer.addresses.exception.NoAddressFoundException;
import com.example.demo.services.customer.addresses.exception.WebserviceConnectionError;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.exception.NoCustomerFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.ConstraintViolationException;

import static com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppErrorHandlerTest implements TestLifecycleLogger {

    @InjectMocks
    private AppErrorHandler handler;


    @Test
    void testHandleRequestMethodNotSupportedException() {
        final NativeWebRequest request;
        final HttpRequestMethodNotSupportedException exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(HttpRequestMethodNotSupportedException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleRequestMethodNotSupportedException(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleMethodArgumentNotValid() {
        final NativeWebRequest request;
        final MethodArgumentNotValidException exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleMethodArgumentNotValid(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleConstraintViolation() {
        final NativeWebRequest request;
        final ConstraintViolationException exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(ConstraintViolationException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleConstraintViolation(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleNoCustomerFoundExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(NoCustomerFoundException.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("Could not find any customer");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleNoCustomerFoundExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Could not find any customer");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleCustomerNotFoundExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(CustomerNotFoundException.class);
        when(exception.getStatus()).thenReturn(Status.NOT_FOUND);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_RECORD_NOT_FOUND.title());
        when(exception.getDetail()).thenReturn("Database record not found");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleCustomerNotFoundExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_RECORD_NOT_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Database record not found");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleCustomerPersistenceFailureExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(CustomerPersistenceFailureException.class);
        when(exception.getStatus()).thenReturn(Status.INTERNAL_SERVER_ERROR);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_RECORD_PERSISTENCE_FAILED.title());
        when(exception.getDetail()).thenReturn("Database record(s) persistence constraint violated");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleCustomerPersistenceFailureExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_RECORD_PERSISTENCE_FAILED.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Database record(s) persistence constraint violated");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseConnectionExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(DataAccessResourceFailureException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseConnectionExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseTransactionExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(CannotCreateTransactionException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseTransactionExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseAccessFailureExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(InvalidDataAccessApiUsageException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseAccessFailureExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseQueryTimeoutExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(QueryTimeoutException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseQueryTimeoutExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleConcurrencyFailureExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(ConcurrencyFailureException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleConcurrencyFailureExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseObjectRetrievalFailureExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(ObjectRetrievalFailureException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseObjectRetrievalFailureExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleDatabaseUnknownDataAccessExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(UncategorizedDataAccessException.class);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleDatabaseUnknownDataAccessExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    /*@Test
    void testHandleNoAccountFoundExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(NoAccountFoundException.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorMessages.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("Could not find any account for customer with the 'customerId':");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleNoAccountFoundExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorMessages.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Could not find any account for customer with the 'customerId':");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }*/

    @Test
    void testHandleAccountInvalidIbanExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(AccountIbanFormatException.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("Customer with the 'customerId': has an invalid 'accountIban':");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleAccountInvalidIbanExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Customer with the 'customerId': has an invalid 'accountIban':");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleWebClientErrorExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(WebclientConnectionError.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("detail");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleWebClientErrorExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("detail");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleNoAddressFoundExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(NoAddressFoundException.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("Could not find any address");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleNoAddressFoundExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Could not find any address");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleAddressNotFoundExceptions() {
        final NativeWebRequest request;
        final AddressNotFoundException exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(AddressNotFoundException.class);
        when(exception.getStatus()).thenReturn(Status.NOT_FOUND);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_RECORD_NOT_FOUND.title());
        when(exception.getDetail()).thenReturn("Could not find address with the 'country':");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleCustomerNotFoundExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_RECORD_NOT_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("Could not find address with the 'country':");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleServiceConnectionExceptions() {
        final NativeWebRequest request;
        final AbstractThrowableProblem exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(WebserviceConnectionError.class);
        when(exception.getStatus()).thenReturn(Status.GONE);
        when(exception.getTitle()).thenReturn(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        when(exception.getDetail()).thenReturn("detail");
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleServiceConnectionExceptions(exception, request);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.GONE);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.DATABASE_NO_RECORD_FOUND.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo("detail");
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    @Test
    void testHandleAllExceptions() {
        final NativeWebRequest request;
        final Exception exception;
        final ResponseEntity<Problem> responseEntity;

        request = mock(NativeWebRequest.class);
        exception = mock(Exception.class);

        when(exception.getMessage()).thenReturn(null);
        when(exception.getCause()).thenReturn(null);
        when(exception.getMessage()).thenReturn("message");

        responseEntity = handler.handleAllExceptions(exception, request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getHeaders()).isNotNull();
        assertThat(responseEntity.getHeaders().isEmpty()).isFalse();
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(responseEntity.hasBody()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getParameters()).isNotNull();
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.title());
        assertThat(responseEntity.getBody().getDetail()).isEqualTo(ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR.detail());
        assertThat(responseEntity.getBody().getParameters()).containsEntry("message", "message");
        assertThat(responseEntity.getBody().getParameters().get("timestamp")).isNotNull();
    }

    /*@Test
    void testErrorResponse() {
        final AppErrorHandler.ErrorResponse errorResponse =
                new AppErrorHandler.ErrorResponse
                        .ErrorResponseBuilder()
                        .name("APPLICATION_GENERIC_UNKNOWN_FAILURE")
                        .title("Application Generic unknown failure")
                        .detail("Application could not handle failure")
                        .type("Exception")
                        .timestamp(DATE_TIME_FORMATTED)
                        .build();

        assertThat(errorResponse.getName()).isNotNull();
        assertThat(errorResponse.getTitle()).isNotNull();
        assertThat(errorResponse.getDetail()).isNotNull();
        assertThat(errorResponse.getType()).isNotNull();
        assertThat(errorResponse.getTimestamp()).isNotNull();
    }

    @Test
    void testErrorResponseNotExist() {
        final AppErrorHandler.ErrorResponse errorResponse =
                new AppErrorHandler.ErrorResponse
                        .ErrorResponseBuilder()
                        .name(null)
                        .title(null)
                        .detail(null)
                        .type(null)
                        .timestamp(null)
                        .build();

        assertThat(errorResponse.getName()).isNull();
        assertThat(errorResponse.getTitle()).isNull();
        assertThat(errorResponse.getDetail()).isNull();
        assertThat(errorResponse.getType()).isNull();
        assertThat(errorResponse.getTimestamp()).isNull();
    }*/

}
