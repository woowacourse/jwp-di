package nextstep.di.scanner;

public interface BeanScanner {
    void doScan(Object... basePackage);

    default void register(Class<?> clazz) {
    }
}
