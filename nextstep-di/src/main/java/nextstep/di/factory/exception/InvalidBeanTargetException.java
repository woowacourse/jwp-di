package nextstep.di.factory.exception;

public class InvalidBeanTargetException extends RuntimeException {
    public InvalidBeanTargetException() {
        super("Error: invalid bean target");
    }

}
