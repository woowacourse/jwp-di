package nextstep.di.factory.exception;

public class CannotCreateBean extends RuntimeException {
    private static final String MESSAGE = "Bean을 생성할 수 없습니다.";

    public CannotCreateBean() {
        super(MESSAGE);
    }
}
