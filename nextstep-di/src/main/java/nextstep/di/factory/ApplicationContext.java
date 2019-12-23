package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.scanner.ComponentScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private final BeanFactory beanFactory;
    private final Class<?> configureClass;
    private Map<Class<?>, Object> beans;

    public ApplicationContext(final Class<?> configureClass) {
        this.configureClass = configureClass;
        this.beanFactory = new BeanFactory();
    }

    public Map<Class<?>, Object> initialize() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configureClass);
        return initializeBeanFactory(configurationBeanScanner);
    }

    private Map<Class<?>, Object> initializeBeanFactory(ConfigurationBeanScanner configurationBeanScanner) {
        ComponentScanner componentScanner = new ComponentScanner(findBasePackages(configurationBeanScanner));
        beanFactory.addScanner(configurationBeanScanner);
        beanFactory.addScanner(componentScanner);
        this.beans = beanFactory.initialize();
        return beans;
    }

    private Object[] findBasePackages(ConfigurationBeanScanner configurationBeanScanner) {
        List<ComponentScan> componentScans = configurationBeanScanner.findComponentScans();

        return componentScans.stream()
            .map(ComponentScan::basePackages)
            .toArray();
    }

    public <T> T getBean(final Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }
}
