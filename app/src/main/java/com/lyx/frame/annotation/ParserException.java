package com.lyx.frame.annotation;

/**
 * ParserException
 * <p>
 * author:  luoyingxing
 * date: 2017/10/16.
 */

public class ParserException extends Exception {

    public ParserException() {
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message) {
        super(message);
    }
}