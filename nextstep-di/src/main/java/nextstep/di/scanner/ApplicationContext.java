package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private List<Scanner> scanners;
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        scanners = new ArrayList<>();
        this.beanFactory = new BeanFactory();
        scanners.add(new ClasspathBeanScanner(beanFactory));
        scanners.add(new ConfigurationBeanScanner(beanFactory));
        register(getBasePackage(configurations));
        initialize();
    }

    private Object[] getBasePackage(Class<?>... configurations) {
        return Arrays.stream(configurations)
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getController() {
        return beanFactory.getController();
    }

    private void register(Object... basePackage) {
        for (Scanner scanner : scanners) {
            scanner.registerPackage(basePackage);
            scanner.registerBeanInfo();
        }
    }

    private void initialize() {
        beanFactory.initialize();
    }
}
