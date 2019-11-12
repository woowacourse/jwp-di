package nextstep.di.factory.exception;

public class IllegalMethodBeanException extends RuntimeException {
    private static final String message = "can't create with method bean";

    public IllegalMethodBeanException() {
        super(message);
    }
}
