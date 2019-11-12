package nextstep.di.factory.exception;

public class NotRegisteredBeanException extends RuntimeException {
    public NotRegisteredBeanException(String message) {
        super(message);
    }
}
