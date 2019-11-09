package nextstep.di.factory;

public class NotFoundDefaultConstructorException extends RuntimeException {

    private static final String NOT_FOUND_DEFAULT_CONSTRUCTOR_MESSAGE = "기본 생성자를 찾을 수 없습니다.";

    public NotFoundDefaultConstructorException() {
        super(NOT_FOUND_DEFAULT_CONSTRUCTOR_MESSAGE);
    }
}
