package nextstep.di.factory.beans;

public class IllegalBeanException extends RuntimeException {
    public IllegalBeanException() {
    }

    public IllegalBeanException(String message) {
        super(message);
    }

    public IllegalBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalBeanException(Throwable cause) {
        super(cause);
    }
}
