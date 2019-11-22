package nextstep.di.scanner;

public interface Scanner {
    void initializeDefinitions();

    void registerPackage(Object... basePackage);

    void registerBeanInfo();
}
