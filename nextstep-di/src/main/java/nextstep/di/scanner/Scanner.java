package nextstep.di.scanner;

public interface Scanner {
    void registerPackage(Object... basePackage);

    void registerBeanInfo();
}
