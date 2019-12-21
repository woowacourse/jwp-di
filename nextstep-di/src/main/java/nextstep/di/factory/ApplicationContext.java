package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.scanner.ClassPathBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;
import nextstep.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

public class ApplicationContext {
    private Class<?>[] configurations;
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        this.configurations = configurations;
        this.beanFactory = initBeanFactory();
        beanFactory.initialize();
    }

    private BeanFactory initBeanFactory() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        configurationBeanScanner.resister(configurations);
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(getBasePackage(configurationBeanScanner));
        return new BeanFactory(configurationBeanScanner, classPathBeanScanner);
    }

    private Object[] getBasePackage(ConfigurationBeanScanner configurationBeanScanner) {
        return Arrays.stream(configurations)
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithType(annotation);
    }
}
