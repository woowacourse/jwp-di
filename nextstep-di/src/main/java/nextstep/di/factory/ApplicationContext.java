package nextstep.di.factory;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(final Class<?>... clazz) {
        beanFactory = new BeanFactory();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);

        configurationBeanScanner.register(clazz);
        classpathBeanScanner.doScan(getBasePackage(clazz));
    }

    public ApplicationContext(final Object... basePackages) {
        beanFactory = new BeanFactory();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        classpathBeanScanner.doScan(basePackages);
    }

    public void initialize() {
        beanFactory.initialize();
    }

    private Object[] getBasePackage(final Class<?>[] clazz) {
        return Arrays.stream(clazz)
                .map(config -> config.getAnnotation(ComponentScan.class))
                .filter(Objects::nonNull)
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public <T> T getBean(final Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getController() {
        return beanFactory.getController();
    }
}
