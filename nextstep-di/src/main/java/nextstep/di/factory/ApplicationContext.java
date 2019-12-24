package nextstep.di.factory;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(final Class<?>... clazz) {
        beanFactory = new BeanFactory();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner();

        Map<Class<?>, BeanDefinition> configureDefinitions = configurationBeanScanner.doScan(clazz);
        Map<Class<?>, BeanDefinition> classBeanDefinitions = classpathBeanScanner.doScan(getBasePackage(clazz));
        configureDefinitions.putAll(classBeanDefinitions);

        beanFactory.init(configureDefinitions);
    }

    public ApplicationContext(final Object... basePackages) {
        beanFactory = new BeanFactory();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner();

        beanFactory.init(classpathBeanScanner.doScan(basePackages));
    }

    private Object[] getBasePackage(final Class<?>[] clazz) {
        return Arrays.stream(clazz)
                .map(config -> config.getAnnotation(ComponentScan.class))
                .filter(Objects::nonNull)
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public void initialize() {
        beanFactory.initialize();
    }

    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getController() {
        return beanFactory.getController();
    }
}
