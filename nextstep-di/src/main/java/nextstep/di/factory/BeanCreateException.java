package nextstep.di.factory;

public class BeanCreateException extends RuntimeException {
    BeanCreateException() {
    }

    BeanCreateException(String message) {
        super(message);
    }

    BeanCreateException(Throwable cause) {
        super(cause);
    }
}
