package com.example.apiTest.Controller;

import com.example.apiTest.Excepetions.AmadeusApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AmadeusApiException.class)
    public ResponseEntity<Object> handleAmadeusApiException(AmadeusApiException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResponse(ex.getStatusCode(), ex.getErrorCode(), ex.getErrorTitle(), ex.getErrorDetail()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), 141, "SYSTEM ERROR HAS OCCURRED", ex.getMessage()));
    }

    static class ErrorResponse {
        private int status;
        private int code;
        private String title;
        private String detail;

        public ErrorResponse(int status, int code, String title, String detail) {
            this.status = status;
            this.code = code;
            this.title = title;
            this.detail = detail;
        }

        public int getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public String getDetail() {
            return detail;
        }
    }
}