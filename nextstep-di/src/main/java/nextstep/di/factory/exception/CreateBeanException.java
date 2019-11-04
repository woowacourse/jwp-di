package nextstep.di.factory.exception;

public class CreateBeanException extends RuntimeException {
    public CreateBeanException(Exception e) {
        super(e);
    }
}
