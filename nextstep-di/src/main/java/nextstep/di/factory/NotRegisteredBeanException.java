package nextstep.di.factory;

public class NotRegisteredBeanException extends RuntimeException {
    public NotRegisteredBeanException() {
        super("Cannot instantiate not registered class!");
    }
}
