package nextstep.exception;

public class ParameterIsNotBeanException extends RuntimeException {
    private static final String PARAMETER_IS_NOT_BEAN_MESSAGE = "생성자의 인자가 빈이 아닙니다.";

    public ParameterIsNotBeanException() {
        super(PARAMETER_IS_NOT_BEAN_MESSAGE);
    }
}
