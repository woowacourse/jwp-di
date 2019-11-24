package nextstep.di.factory;

public class NotSupportedInjectionTypeException extends RuntimeException {
    public NotSupportedInjectionTypeException() {
        super("지원하지 않는 인젝션 타입");
    }
}
