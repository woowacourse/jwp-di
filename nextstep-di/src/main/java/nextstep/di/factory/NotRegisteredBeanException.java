package nextstep.di.factory;

public class NotRegisteredBeanException extends RuntimeException {
    public NotRegisteredBeanException(String message) {
        super(message);
    }
}
