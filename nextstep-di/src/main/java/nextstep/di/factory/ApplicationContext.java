package nextstep.di.factory;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Map;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configurations);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(getBasePackages(configurations));

        this.beanFactory = new BeanFactory(configurationBeanScanner, classpathBeanScanner);
    }

    private Object[] getBasePackages(Class<?>... configurations) {
        return Arrays.stream(configurations)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class).value())
                .flatMap(Arrays::stream)
                .toArray();
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        return beanFactory.getControllers();
    }
}
