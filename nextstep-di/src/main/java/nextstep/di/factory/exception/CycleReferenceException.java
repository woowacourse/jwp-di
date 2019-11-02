package nextstep.di.factory.exception;

public class CycleReferenceException extends RuntimeException{
    private static final String message = "순환참조가 있습니다.";

    public CycleReferenceException() {
        super(message);
    }
}
