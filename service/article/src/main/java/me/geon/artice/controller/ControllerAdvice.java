package me.geon.artice.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.geon.artice.ApiResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 400 BadRequest
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> handleBadRequest(BindException e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            InvalidFormatException.class,
            ServletRequestBindingException.class
    })
    protected ApiResponse<Object> handleInvalidFormatException(final Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 404 NotFound
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    protected ApiResponse<Object> handleNotFoundException(final ChangeSetPersister.NotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * 405 Method Not Supported
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ApiResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    /**
     * 415 UnSupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeException.class)
    protected ApiResponse<Object> handleHttpMediaTypeException(final HttpMediaTypeException e) {
        return ApiResponse.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }


    /**
     * 500 Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResponse<Object> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
