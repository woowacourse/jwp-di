package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?> configuration) {
        Object[] basePackage = configuration.getAnnotation(ComponentScan.class).value();
        initialize(basePackage);
    }

    public void initialize(Object... basePackage) {
        beanFactory = new BeanFactory();
        new ClasspathBeanScanner(beanFactory, basePackage);
        new ConfigurationBeanScanner(beanFactory, basePackage);
        beanFactory.initialize();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
