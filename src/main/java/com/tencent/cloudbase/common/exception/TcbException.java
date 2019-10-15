package com.tencent.cloudbase.common.exception;

public class TcbException extends Exception{
    private String errorCode = "";
    private String requestId = "";
    private String message = "";

    public TcbException(String errorCode, String message) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public TcbException(String errorCode, String message, String requestId) {
        super(message);
        this.errorCode = errorCode;
        this.requestId = requestId;
    }


    public String toString() {
        return "Code: " + errorCode + " " + message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
