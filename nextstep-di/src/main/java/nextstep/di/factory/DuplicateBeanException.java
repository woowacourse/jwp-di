package nextstep.di.factory;

public class DuplicateBeanException extends RuntimeException {
    public DuplicateBeanException() {
        super("같은 빈을 두 번 생성 할 수 있습니다.");
    }
}
