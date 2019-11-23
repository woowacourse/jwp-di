package nextstep.exception;

public class BeanCreationException extends RuntimeException {
    public BeanCreationException(Throwable cause) {
        super(cause);
    }

    public BeanCreationException(String message) {
        super(message);
    }
}
