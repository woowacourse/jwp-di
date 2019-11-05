package nextstep.exception;

public class DefaultConstructorFindFailException extends RuntimeException {
    private static final String DEFAULT_CONSTRUCTOR_FIND_FAIL_MESSAGE = "기본 생성자 찾기에 실패하였습니다.";

    public DefaultConstructorFindFailException() {
        super(DEFAULT_CONSTRUCTOR_FIND_FAIL_MESSAGE);
    }
}
