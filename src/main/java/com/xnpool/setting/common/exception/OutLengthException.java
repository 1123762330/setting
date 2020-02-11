package com.xnpool.setting.common.exception;

public class OutLengthException extends ServiceException {
    public OutLengthException() {
        super();
    }

    public OutLengthException(String message) {
        super(message);
    }

    public OutLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutLengthException(Throwable cause) {
        super(cause);
    }

    protected OutLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
