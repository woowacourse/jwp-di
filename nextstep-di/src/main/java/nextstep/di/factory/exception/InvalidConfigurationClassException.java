package nextstep.di.factory.exception;

public class InvalidConfigurationClassException extends RuntimeException {
    private static final String ERROR_MSG = "빈 설정 클래스는 @Configuration 어노테이션을 붙여야 합니다.";

    public InvalidConfigurationClassException() {
        super(ERROR_MSG);
    }
}
