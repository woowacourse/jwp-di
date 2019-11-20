package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContext {

    private BeanFactory beanFactory;

    public ApplicationContext() {
        this.beanFactory = new BeanFactory();
    }

    public Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getAnnotatedWith(annotation);
    }

    public void configurations(Class<?> configurationClass) {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(configurationClass);
    }

    public void lookUpContext(Object... basePackage) {
        configurations(basePackage);
        components(basePackage);
    }

    public void initialize() {
        this.beanFactory.initialize();
    }

    private void configurations(Object... basePackage) {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.doScan(basePackage);
    }

    private void components(Object... basePackage) {
        ClasspathBeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan(basePackage);
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanFactory.getBean(beanClass);
    }
}
