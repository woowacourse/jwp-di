package nextstep.di.factory;

public class NotFoundBeanException extends RuntimeException{
    public NotFoundBeanException() {
        super();
    }

    public NotFoundBeanException(String message) {
        super(message);
    }
}
