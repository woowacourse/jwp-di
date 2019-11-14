package nextstep.di.scanner;

public class ClassNotConfigurationException extends RuntimeException {
    public ClassNotConfigurationException() {
        super("Configuration이 아닌 class 입니다.");
    }
}
