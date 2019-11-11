package nextstep.di.exception;

public class BeanIncludingCycleException extends RuntimeException {
    public BeanIncludingCycleException() {
        super("사이클이 존재합니다.");
    }
}
