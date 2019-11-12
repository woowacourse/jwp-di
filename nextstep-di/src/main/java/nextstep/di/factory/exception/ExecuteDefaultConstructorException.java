package nextstep.di.factory.exception;

public class ExecuteDefaultConstructorException extends RuntimeException {
    public ExecuteDefaultConstructorException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
