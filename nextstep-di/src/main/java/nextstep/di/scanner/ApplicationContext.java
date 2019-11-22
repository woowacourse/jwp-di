package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;

import java.util.Map;

public class ApplicationContext {
    private ClasspathBeanScanner cpbs;
    private ConfigurationBeanScanner cbs;
    private BeanFactory beanFactory;

    public ApplicationContext(Object... basePackage) {
        this.beanFactory = new BeanFactory();
        this.cpbs = new ClasspathBeanScanner(beanFactory);
        this.cbs = new ConfigurationBeanScanner(beanFactory);
        register(basePackage);
        initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getController() {
        return beanFactory.getController();
    }

    private void register(Object... basePackage) {
        cpbs.registerPackage(basePackage);
        cbs.registerPackage(basePackage);
        cpbs.registerBeanInfo();
        cbs.registerBeanInfo();
    }

    private void initialize() {
        beanFactory.initialize();
    }
}
