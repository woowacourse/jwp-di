package nextstep.di.factory.exception;

public class DoesNotAllowMultipleInjectedConstructorException extends RuntimeException {
    private static final String MESSAGE = "Inject 애너테이션은 하나의 생성자에만 선언할 수 있습니다.";

    public DoesNotAllowMultipleInjectedConstructorException() {
        super(MESSAGE);
    }
}
