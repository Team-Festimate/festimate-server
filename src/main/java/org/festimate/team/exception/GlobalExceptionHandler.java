package org.festimate.team.exception;

import lombok.extern.slf4j.Slf4j;
import org.festimate.team.common.response.ApiResponse;
import org.festimate.team.common.response.ResponseBuilder;
import org.festimate.team.common.response.ResponseError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(FestimateException.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(FestimateException e) {
        log.error("FestimateException occurred", e);
        return ResponseBuilder.error(e.getResponseError());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred", e);
        return ResponseEntity
                .status(ResponseError.BAD_REQUEST.getHttpStatus())
                .body(ApiResponse.error(ResponseError.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException occurred", e);
        return ResponseBuilder.error(ResponseError.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException occurred", e);
        return ResponseBuilder.error(ResponseError.INVALID_REQUEST_PARAMETER);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Exception occurred", e);
        return ResponseBuilder.error(ResponseError.INTERNAL_SERVER_ERROR);
    }
}