package nextstep.exception;

public class BeanMethodNotFoundException extends RuntimeException{
    private static final String BEAN_METHOD_NOT_FOUND_MESSAGE = "설정 파일에서 Bean메서드를 찾을 수 없습니다.";

    public BeanMethodNotFoundException() {
        super(BEAN_METHOD_NOT_FOUND_MESSAGE);
    }
}
