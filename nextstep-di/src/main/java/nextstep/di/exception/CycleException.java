package nextstep.di.exception;

public class CycleException extends RuntimeException {
    public CycleException() {
        super("사이클입니다!");
    }
}
