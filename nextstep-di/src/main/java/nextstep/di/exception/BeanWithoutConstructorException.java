package nextstep.di.exception;

public class BeanWithoutConstructorException extends RuntimeException {
    private BeanWithoutConstructorException(String s) {
        super(s);
    }

    public static BeanWithoutConstructorException from(Class<?> type) {
        String s = String.format("%s 에서 사용할 수 있는 생성자가 존재하지 않습니다. ", type);
        return new BeanWithoutConstructorException(s);
    }
}
