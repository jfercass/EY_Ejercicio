package test.ey.user.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import test.ey.user.exception.UserException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller to handle the exceptions.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    protected ResponseEntity<Object> badRequest(
            RuntimeException ex, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = getBody(httpStatus, ex, request);

        return new ResponseEntity<>(body, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler(value = {UserException.class})
    @ResponseBody
    protected ResponseEntity<Object> restTemplateException(
            RuntimeException ex, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Map<String, Object> body = getBody(httpStatus, ex, request);

        return new ResponseEntity<>(body, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler(value = {RestClientException.class})
    @ResponseBody
    protected ResponseEntity<Object> restClinentException(
            RuntimeException ex, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        Map<String, Object> body = getBody(httpStatus, ex, request);

        return new ResponseEntity<>(body, new HttpHeaders(), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> body = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            body.put("message", errorMessage);
        });

        return new ResponseEntity<>(body, new HttpHeaders(), httpStatus);
    }

    private Map<String, Object> getBody(HttpStatus httpStatus, RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());

        return body;
    }

}
