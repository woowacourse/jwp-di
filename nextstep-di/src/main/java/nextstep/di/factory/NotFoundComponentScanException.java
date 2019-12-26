package nextstep.di.factory;

public class NotFoundComponentScanException extends RuntimeException {
    public NotFoundComponentScanException() {
        super("compinent Scan을 찾을 수 없습니다.");
    }
}
