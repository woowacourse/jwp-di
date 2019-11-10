package nextstep.di.factory;

public class BeanFactoryInitializer {
    @SuppressWarnings("unchecked")
    public static void init(BeanScannerConfig beanScannerConfig) {
        BeanScanner beanScanner = new BeanScanner(beanScannerConfig);
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.initialize(beanScanner.getTypesAnnotatedWith());
    }
}
