package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanScanner;
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
        BeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(configurationClass);
    }

    public void initialize(Object... basePackage) {
        configurations(basePackage);
        components(basePackage);
        this.beanFactory.initialize();
    }

    private void configurations(Object... basePackage) {
        BeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.doScan(basePackage);
    }

    private void components(Object... basePackage) {
        BeanScanner cbds = new ClasspathBeanScanner(beanFactory);
        cbds.doScan(basePackage);
    }

    public <T> T getBean(Class<T> beanClass) {
        return beanFactory.getBean(beanClass);
    }
}
