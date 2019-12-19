package nextstep.di.factory.exception;

public class NotRegisteredBeanException extends RuntimeException {
    public NotRegisteredBeanException() {
        super("Cannot instantiate not registered class!");
    }
}
