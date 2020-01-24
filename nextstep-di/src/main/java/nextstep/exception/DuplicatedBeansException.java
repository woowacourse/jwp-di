package nextstep.exception;

public class DuplicatedBeansException extends RuntimeException {
    private static final String DUPLICATED_BEANS_MESSAGE = "이미 존재하는 빈입니다. : %s";

    public DuplicatedBeansException(String beanName) {
        super(String.format(DUPLICATED_BEANS_MESSAGE, beanName));
    }
}
