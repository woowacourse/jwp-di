package nextstep.di.factory;

public class UninitializedBeanFactoryException extends RuntimeException {
    private static final String ERROR_MSG = "초기화되지 않은 빈 팩토리에 접근할 수 없습니다.";

    public UninitializedBeanFactoryException() {
        super(ERROR_MSG);
    }
}
