package ua.agwebs.web.exceptions;

/**
 * Created by partiza on 4/14/17.
 */
public class PocketBalanceIllegalAccessException extends RuntimeException {

    public PocketBalanceIllegalAccessException() {
    }

    public PocketBalanceIllegalAccessException(String message) {
        super(message);
    }

    public PocketBalanceIllegalAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public PocketBalanceIllegalAccessException(Throwable cause) {
        super(cause);
    }

    public PocketBalanceIllegalAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
