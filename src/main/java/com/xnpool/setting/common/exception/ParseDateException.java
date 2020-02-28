package com.xnpool.setting.common.exception;

public class ParseDateException extends ServiceException {
    private static final long serialVersionUID = -6755389861451969974L;

    public ParseDateException() {
        super();
    }

    public ParseDateException(String message) {
        super(message);
    }

    public ParseDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseDateException(Throwable cause) {
        super(cause);
    }

    protected ParseDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
