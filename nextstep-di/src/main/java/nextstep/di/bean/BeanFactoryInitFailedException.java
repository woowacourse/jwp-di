package nextstep.di.bean;

public class BeanFactoryInitFailedException extends RuntimeException {
    public BeanFactoryInitFailedException(Throwable e) {
        super(e);
    }
}
