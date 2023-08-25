package com.developer.maker.dmaker.exception;

import static com.developer.maker.dmaker.code.DMakerErrorCode.INTERNAL_SERVER_ERROR;
import static com.developer.maker.dmaker.code.DMakerErrorCode.INVALID_REQUEST;

import com.developer.maker.dmaker.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e,
        HttpServletRequest request){
        log.error("errorCode: {}, url: {}, message: {}",
            e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
            .errorCode(e.getDMakerErrorCode())
            .errorMessage(e.getDetailMessage())
            .build();
    }

    @ExceptionHandler(value = {
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
        Exception e, HttpServletRequest request
    ){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
            .errorCode(INVALID_REQUEST)
            .errorMessage(INVALID_REQUEST.getMessage())
            .build();
    }

    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
        Exception e, HttpServletRequest request
    ){
        log.error("url: {}, message: {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
            .errorCode(INTERNAL_SERVER_ERROR)
            .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
            .build();
    }
}
