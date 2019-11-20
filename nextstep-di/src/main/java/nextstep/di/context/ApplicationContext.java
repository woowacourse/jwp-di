package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

public class ApplicationContext {

    private BeanFactory beanFactory;

    public ApplicationContext() {
        this.beanFactory = new BeanFactory();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void configurations(Class<?> configurationClass) {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(configurationClass);

    }

    public void components(Object... basePackage) {
        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan(basePackage);
    }

    public void initialize() {
        this.beanFactory.initialize();
    }
}
