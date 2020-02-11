package com.xnpool.setting.common.exception;

public class NoMessageException extends ServiceException {
    public NoMessageException() {
        super();
    }

    public NoMessageException(String message) {
        super(message);
    }

    public NoMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMessageException(Throwable cause) {
        super(cause);
    }

    protected NoMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
