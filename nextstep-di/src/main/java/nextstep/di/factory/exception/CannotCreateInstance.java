package nextstep.di.factory.exception;

public class CannotCreateInstance extends RuntimeException {
    private static final String MESSAGE = "생성자를 생성할 수 없습니다.";

    public CannotCreateInstance(final Throwable cause) {
        super(MESSAGE, cause);
    }
}
