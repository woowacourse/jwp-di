package nextstep.di.factory;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;
import java.util.Map;

public class ApplicationContext {
    private BeanFactory beanFactory;
    private ClasspathBeanScanner classpathBeanScanner;
    private ConfigurationBeanScanner configurationBeanScanner;

    public ApplicationContext(Class<?>... configurations) {
        beanFactory = new BeanFactory();
        classpathBeanScanner = new ClasspathBeanScanner(beanFactory);
        configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);

        classpathBeanScanner.doScan(getBasePackages(configurations));
        configurationBeanScanner.register(configurations);
        beanFactory.initialize();
    }

    private Object[] getBasePackages(Class<?>[] configurations) {
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
