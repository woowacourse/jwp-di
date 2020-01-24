package nextstep.exception;

public class EmptyBasePackagesException extends RuntimeException {
    private static final String EMPTY_BASE_PACKAGES_MESSAGE = "Bean Scan을 할 수 없습니다.";

    public EmptyBasePackagesException() {
        super(EMPTY_BASE_PACKAGES_MESSAGE);
    }
}
