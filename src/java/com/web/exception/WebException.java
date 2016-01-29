package com.web.exception;

public class WebException extends java.lang.Exception {

    private int code;

    public WebException(int code) {
        this.code = code;
    }
    public WebException(int code, String message) {
        super(message);
        this.code = code;
    }
    public WebException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}