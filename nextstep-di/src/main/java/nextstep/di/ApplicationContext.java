package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanScanners;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?> configuration) {
        Object[] basePackage = configuration.getAnnotation(ComponentScan.class).value();
        initialize(basePackage);
    }

    public void initialize(Object... basePackage) {
        BeanScanners beanScanners = new BeanScanners(new ClasspathBeanScanner(basePackage),
                new ConfigurationBeanScanner(basePackage));
        beanFactory = new BeanFactory(beanScanners);

        beanFactory.initialize();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
