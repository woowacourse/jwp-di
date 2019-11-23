package nextstep.di.factory.domain.context;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.domain.scanner.ClassPathScanner;
import nextstep.di.factory.domain.scanner.ConfigurationScanner;
import nextstep.di.factory.support.SupportedClass;

import java.lang.annotation.Annotation;
import java.util.Set;

public class GenericApplicationContext implements ApplicationContext {
    private Class<?> configuration;
    private BeanFactory beanFactory;
    private SupportedClass supportedClass;

    public GenericApplicationContext(Class<?> configuration) {
        this.configuration = configuration;
        this.beanFactory = new GenericBeanFactory();
        this.supportedClass = new SupportedClass();
    }

    public GenericApplicationContext(Class<?> configuration, BeanFactory beanFactory) {
        this.configuration = configuration;
        this.beanFactory = beanFactory;
        this.supportedClass = new SupportedClass();
    }

    @Override
    public void initialize() {
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);

        if (configuration.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configuration.getAnnotation(ComponentScan.class);
            ClassPathScanner classPathScanner = new ClassPathScanner(componentScan.value());
            classPathScanner.scan(beanFactory);
            classPathScanner.scan(supportedClass);
        }

        if (configuration.isAnnotationPresent(Configuration.class)) {
            configurationScanner.register(configuration);
        }
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return supportedClass.getClassByAnnotation(annotation);
    }
}
