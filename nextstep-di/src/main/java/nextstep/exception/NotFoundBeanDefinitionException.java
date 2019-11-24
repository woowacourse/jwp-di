package nextstep.exception;

public class NotFoundBeanDefinitionException extends RuntimeException {
    private static final String MESSAGE = "BeanDefinition을 찾지 못했습니다.";

    public NotFoundBeanDefinitionException() {
        super(MESSAGE);
    }
}
