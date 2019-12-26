package nextstep.di.factory;

public class InvalidBeanException extends RuntimeException {
    public InvalidBeanException() {
        super("생성할 수 없는 빈입니다.");
    }
}
