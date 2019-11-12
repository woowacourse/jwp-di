package nextstep.di.factory.exception;

public class InstantiateBeansException extends RuntimeException {
    public InstantiateBeansException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
