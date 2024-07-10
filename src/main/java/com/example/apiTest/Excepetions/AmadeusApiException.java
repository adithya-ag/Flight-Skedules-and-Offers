package com.example.apiTest.Excepetions;

public class AmadeusApiException extends RuntimeException {
    private int statusCode;
    private int errorCode;
    private String errorTitle;
    private String errorDetail;

    public AmadeusApiException(int statusCode, int errorCode, String errorTitle, String errorDetail) {
        super(errorTitle + ": " + errorDetail);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorTitle = errorTitle;
        this.errorDetail = errorDetail;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorDetail() {
        return errorDetail;
    }
}