package ua.agwebs.root.service.specifications;


public class UnsupportableSearchCriteriaException extends RuntimeException {

    public UnsupportableSearchCriteriaException() {
        super();
    }

    public UnsupportableSearchCriteriaException(String message) {
        super(message);
    }

    public UnsupportableSearchCriteriaException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportableSearchCriteriaException(Throwable cause) {
        super(cause);
    }

    protected UnsupportableSearchCriteriaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
