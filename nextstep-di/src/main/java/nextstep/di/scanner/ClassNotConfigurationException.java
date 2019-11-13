package nextstep.di.scanner;

public class ClassNotConfigurationException extends RuntimeException {
    public ClassNotConfigurationException() {
        super("적절하지 않은 Configuration 클래스 입니다.");
    }
}
