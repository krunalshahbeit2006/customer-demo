package com.example.demo.services.customer.web.handler;

import com.example.demo.services.customer.accounts.exception.AccountIbanFormatException;
import com.example.demo.services.customer.accounts.exception.NoAccountFoundException;
import com.example.demo.services.customer.accounts.exception.WebclientConnectionError;
import com.example.demo.services.customer.addresses.exception.AddressNotFoundException;
import com.example.demo.services.customer.addresses.exception.NoAddressFoundException;
import com.example.demo.services.customer.addresses.exception.WebserviceConnectionError;
import com.example.demo.services.customer.exception.CustomerNotFoundException;
import com.example.demo.services.customer.exception.CustomerPersistenceFailureException;
import com.example.demo.services.customer.exception.NoCustomerFoundException;
import com.example.demo.services.customer.util.AppLoggerUtil;
import com.example.demo.services.customer.util.AppUtil;
import com.example.demo.services.customer.web.constant.AppResponseMessage.ErrorType;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.SpringAdviceTrait;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
public class AppErrorHandler implements ProblemHandling, SpringAdviceTrait {

    private static HttpHeaders headers() {
        final HttpHeaders headers;

        headers = new HttpHeaders();
        headers.setContentType(MediaTypes.HTTP_PROBLEM_DETAILS_JSON);

        return headers;
    }

    /*private static Problem problem(final Exception ex, final HttpStatus httpStatus) {
        final Map<String, String> properties;

        properties = new HashMap<>();
        properties.put("name", errorMessages.name());
        properties.put("errorType", errorType(ex));
        properties.put("timestamp", DATE_TIME_FORMATTER.format(LocalDateTime.now()));

        return Problem.create()
                .withStatus(httpStatus)
                .withTitle(errorMessages.title())
                .withDetail(errorMessages.detail())
                .withProperties(properties);
    }*/

    /*private static ErrorResponse errorResponse(final Exception ex, final HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .status(httpStatus)
                .name(errorMessages.name())
                .title(errorMessages.title())
                .detail(errorMessages.detail())
                .type(errorType(ex))
                .timestamp(DATE_TIME_FORMATTER.format(LocalDateTime.now()))
                .build();
    }*/

    // The causal chain of causes is disabled by default,
    // but we can easily enable it by overriding the behavior:
    /*@Override
    public boolean isCausalChainsEnabled() {
        return true;
    }*/

    /*@Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().
                registerModules(new ProblemModule(),
                        new ConstraintViolationProblemModule());
    }*/

    @Override
    public final ResponseEntity<Problem> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.APPLICATION_REQUEST_FIELD_INVALID);

        return this.handleViolations(ex, request, HttpStatus.BAD_REQUEST);
    }

    @Override
    public final ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.APPLICATION_REQUEST_FIELD_INVALID);

        return this.handleViolations(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<Problem> handleConstraintViolation(final ConstraintViolationException ex, final NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.APPLICATION_REQUEST_FIELD_INVALID);

        return this.handleViolations(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({NoCustomerFoundException.class})
    public final ResponseEntity<Problem> handleNoCustomerFoundExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.DATABASE_NO_RECORD_FOUND);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({CustomerNotFoundException.class})
    public final ResponseEntity<Problem> handleCustomerNotFoundExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.DATABASE_RECORD_NOT_FOUND);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({CustomerPersistenceFailureException.class})
    public final ResponseEntity<Problem> handleCustomerPersistenceFailureExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_RECORD_PERSISTENCE_FAILED);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({CannotGetJdbcConnectionException.class, DataAccessResourceFailureException.class})
    public final ResponseEntity<Problem> handleDatabaseConnectionExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_CONNECTION_CLOSED_ERROR);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({CannotCreateTransactionException.class})
    public final ResponseEntity<Problem> handleDatabaseTransactionExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_TRANSACTION_NOT_CREATED);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public final ResponseEntity<Problem> handleDatabaseAccessFailureExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_QUERY_NOT_COMPILED);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({QueryTimeoutException.class})
    public final ResponseEntity<Problem> handleDatabaseQueryTimeoutExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_QUERY_OPERATION_TIMEOUT);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({ConcurrencyFailureException.class})
    public final ResponseEntity<Problem> handleConcurrencyFailureExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_RECORD_LOCKING_FAILED);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({ObjectRetrievalFailureException.class})
    public final ResponseEntity<Problem> handleDatabaseObjectRetrievalFailureExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_RECORD_RETRIEVAL_FAILED);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({UncategorizedDataAccessException.class})
    public final ResponseEntity<Problem> handleDatabaseUnknownDataAccessExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.DATABASE_RECORD_UNKNOWN_ACCESS);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({NoAccountFoundException.class})
    public final ResponseEntity<Problem> handleNoAccountFoundExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.WEBSERVICE_NO_RECORD_FOUND);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({AccountIbanFormatException.class})
    public final ResponseEntity<Problem> handleAccountInvalidIbanExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.WEBSERVICE_RECORD_FIELD_INVALID);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({WebclientConnectionError.class})
    public final ResponseEntity<Problem> handleWebClientErrorExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.WEBSERVICE_SERVER_CONNECTION_ERROR);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({NoAddressFoundException.class})
    public final ResponseEntity<Problem> handleNoAddressFoundExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.WEBSERVICE_NO_RECORD_FOUND);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({AddressNotFoundException.class})
    public final ResponseEntity<Problem> handleAddressNotFoundExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logWarning(ex, ErrorType.WEBSERVICE_RECORD_NOT_FOUND);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({WebserviceConnectionError.class})
    public final ResponseEntity<Problem> handleServiceConnectionExceptions(final AbstractThrowableProblem ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.WEBSERVICE_SERVER_CONNECTION_ERROR);

        return this.handleExceptions(ex, request);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Problem> handleAllExceptions(final Exception ex, final NativeWebRequest request) {
        AppLoggerUtil.logError(ex, ErrorType.INTERNAL_SERVER_UNKNOWN_ERROR);

        return this.handleExceptions(ex, request);
    }

    private ResponseEntity<Problem> handleViolations(final Exception ex, final NativeWebRequest request, final HttpStatus httpStatus) {
        return this.handleTechnicalException(ex, request, httpStatus);
    }

    private ResponseEntity<Problem> handleExceptions(final Exception ex, final NativeWebRequest request) {
        return (ex instanceof AbstractThrowableProblem) ?
                this.handleBusinessExceptions((AbstractThrowableProblem) ex, request) :
                this.handleTechnicalException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Problem> handleBusinessExceptions(final AbstractThrowableProblem throwable, final NativeWebRequest request) {
        return this.create(throwable, this.problem(throwable), request, headers());
    }

    private ResponseEntity<Problem> handleTechnicalException(final Exception ex, final NativeWebRequest request, final HttpStatus httpStatus) {
        return this.create(ex, this.problem(ex, httpStatus), request, headers());
    }

    private Problem problem(final AbstractThrowableProblem ex) {
        return Problem.builder()
                .withStatus(ex.getStatus())
                .withTitle(ex.getTitle())
                .withDetail(ex.getDetail())
                .withType(ex.getType())
                .withInstance(ex.getInstance())
                .withCause(ex.getCause())
                .with("message", ex.getMessage())
                .with("timestamp", AppUtil.DATE_TIME_FORMATTER.format(LocalDateTime.now()))
                .build();
    }

    private Problem problem(final Exception ex, final HttpStatus httpStatus) {
        String detail;

        detail = ex.getCause() == null ? "Application could not handle failure." : ex.getCause().getMessage();
        return Problem.builder()
                .withType(URI.create("https://zalando.github.io/problem/constraint-violation"))
                .withStatus(toStatus(httpStatus))
                .withTitle("Application Generic unknown failure")
                .withDetail(detail)
                .with("message", ex.getMessage())
                .with("timestamp", AppUtil.DATE_TIME_FORMATTER.format(LocalDateTime.now()))
                .build();
    }

    /*@Data
    @Builder
    static class ErrorResponse {

        @NotEmpty
        private HttpStatus status;

        @NotBlank
        private String name;

        @NotBlank
        private String title;

        @NotBlank
        private String detail;

        @NotBlank
        private String type;

        @NotBlank
        private String timestamp;
    }*/

}
