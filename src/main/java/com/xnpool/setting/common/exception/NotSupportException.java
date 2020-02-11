package com.xnpool.setting.common.exception;

public class NotSupportException extends ServiceException {

    private static final long serialVersionUID = 6053949872033155240L;

    public NotSupportException() {
        super();
    }

    public NotSupportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportException(String message) {
        super(message);
    }

    public NotSupportException(Throwable cause) {
        super(cause);
    }
}
