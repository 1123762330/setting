package com.xnpool.setting.common.exception;

public class DataExistException extends ServiceException {
    private static final long serialVersionUID = -6312338397944394237L;

    public DataExistException() {
        super();
    }

    public DataExistException(String message) {
        super(message);
    }

    public DataExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExistException(Throwable cause) {
        super(cause);
    }

    protected DataExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
