package nextstep.di.exception;

public class BeanNotExistException extends RuntimeException {
    public BeanNotExistException() {
        super("Bean Not Exists");
    }
}
