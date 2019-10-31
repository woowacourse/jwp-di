package nextstep.di.factory;

public class BeanCreateException extends RuntimeException {
    public BeanCreateException() {
    }

    public BeanCreateException(String message) {
        super(message);
    }

    public BeanCreateException(Throwable cause) {
        super(cause);
    }
}
