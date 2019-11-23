package nextstep.di.factory.beans;

public class FailToCreateBeanException extends RuntimeException {
    public FailToCreateBeanException() {
    }

    public FailToCreateBeanException(String message) {
        super(message);
    }

    public FailToCreateBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailToCreateBeanException(Throwable cause) {
        super(cause);
    }
}
