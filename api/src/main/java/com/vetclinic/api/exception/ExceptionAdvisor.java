package com.vetclinic.api.exception;

import com.vetclinic.api.dto.ResponseError;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.*;

@Log4j2
@ControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<FiledValidationException> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.add(new FiledValidationException(field, message));
        });

        ResponseError errorResponse = ResponseError.builder()
                .message("Validation error")
                .reason(errors.get(0).getReason())
                .details(request.getContextPath().isEmpty() ? null: request.getContextPath())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ BadRequest.class })
    public ResponseEntity<?> handleBadRequestException(BadRequest ex, WebRequest webRequest) {
        String trxId = MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .trxId(trxId)
                .build();
        log.error("BasRequest exception occur, reason={}, details={}, message={}",
                response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler({ Ineligible.class })
    public ResponseEntity<?> handleIneligibleException(Ineligible ex, WebRequest webRequest) {
        String trxId = MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .trxId(trxId)
                .build();
        log.error("Ineligible exception occur, reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler({ NotFound.class })
    public ResponseEntity<?> handleNotFoundException(NotFound ex, WebRequest webRequest) {
        String trxId = MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .trxId(trxId)
                .build();
        log.error("NotFound exception occur, reason={}, details={}, message={}",
                response.getReason(), response.getDetails(), response.getMessage());
        MDC.clear();
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler({ InternalServerError.class })
    public ResponseEntity<?> handleInternalSystemErrorException(InternalServerError ex, WebRequest webRequest) {
        String trxId = MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .details(webRequest.getContextPath())
                .trxId(trxId)
                .build();
        log.error("InternalServerError exception occur, reason={}, details={}, message={}",
                response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

}