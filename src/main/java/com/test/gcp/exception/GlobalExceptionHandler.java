package com.test.gcp.exception;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.test.gcp.payload.ErrorDetails;

import lombok.Generated;

@RestControllerAdvice @Generated
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND) @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorDetails handleResourceNotFoundException(final ResourceNotFoundException exception) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(getCurrentDateTime());
        errorDetails.setErrorMessage(exception.getMessage());
        return errorDetails;
    }

    @ResponseStatus(HttpStatus.CONFLICT) @ExceptionHandler(ResourceFoundException.class)
    public ErrorDetails handleResourceFoundException(final ResourceFoundException exception) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(getCurrentDateTime());
        errorDetails.setErrorMessage(exception.getMessage());
        return errorDetails;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN) @ExceptionHandler(BlogAPIException.class)
    public ErrorDetails handleBlogAPIException(final BlogAPIException exception) {

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(getCurrentDateTime());
        errorDetails.setErrorMessage(exception.getMessage());
        return errorDetails;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorDetails> handleValidationExceptions(final MethodArgumentNotValidException ex) {

        List<ErrorDetails> errorDetails = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            ErrorDetails errorDetail = new ErrorDetails();
            errorDetail.setTimestamp(getCurrentDateTime());
            errorDetail.setErrorMessage(error.getDefaultMessage());
            errorDetail.setFieldName(((FieldError) error).getField());
            errorDetails.add(errorDetail);
        });
        return errorDetails;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) @ExceptionHandler(Exception.class)
    public Object handleAnyException(final Exception e) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", getCurrentDateTime());
        body.put("message", e.getLocalizedMessage());

        return body;
    }

    private static String getCurrentDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
}
