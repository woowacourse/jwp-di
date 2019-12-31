package nextstep.di.factory.exception;

public class BeanCreationFailException extends RuntimeException {
    private static final String MESSAGE = "빈 생성에 실패했습니다.";


    public BeanCreationFailException(ReflectiveOperationException e) {
        super(MESSAGE, e);
    }
}
