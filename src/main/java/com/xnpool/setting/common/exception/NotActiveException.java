package com.xnpool.setting.common.exception;

public class NotActiveException extends ServiceException {


    private static final long serialVersionUID = -2090254862357095557L;

    public NotActiveException() {
        super();
    }

    public NotActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotActiveException(String message) {
        super(message);
    }

    public NotActiveException(Throwable cause) {
        super(cause);
    }
}
