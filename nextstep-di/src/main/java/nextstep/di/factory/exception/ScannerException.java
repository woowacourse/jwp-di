package nextstep.di.factory.exception;

public class ScannerException extends RuntimeException {
    public ScannerException(Exception e) {
        super(e.getMessage() + " 에러");
    }
}
