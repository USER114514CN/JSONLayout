package com.user114514.jsonlayout.core;

public class UnknownAttributeException extends RuntimeException {
    
    public UnknownAttributeException() {
        super();
    }

    public UnknownAttributeException(String message) {
        super(message);
    }

    public UnknownAttributeException(Throwable th) {
        super(th);
    }

    public UnknownAttributeException(String message, Throwable th) {
        super(message, th);
    }

}
