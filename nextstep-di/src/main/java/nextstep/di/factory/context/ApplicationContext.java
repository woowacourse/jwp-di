package nextstep.di.factory.context;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.beans.ComponentBeanScanner;
import nextstep.di.factory.beans.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?> entryPoint) {
        beanFactory = new BeanFactory();
        ConfigurationBeanScanner configScanner = new ConfigurationBeanScanner(entryPoint);
        initializeBeanFactory(configScanner);
    }

    public ApplicationContext(Object... basePackages) {
        beanFactory = new BeanFactory();
        ConfigurationBeanScanner configScanner = new ConfigurationBeanScanner(basePackages);
        initializeBeanFactory(configScanner);
    }

    private void initializeBeanFactory(ConfigurationBeanScanner configScanner) {
        ComponentBeanScanner componentScanner = new ComponentBeanScanner(findBasePackages(configScanner));
        beanFactory.addScanner(configScanner);
        beanFactory.addScanner(componentScanner);
        beanFactory.initialize();
    }

    private Object[] findBasePackages(ConfigurationBeanScanner configScanner) {
        List<ComponentScan> componentScans = configScanner.findComponentScans();

        return componentScans.stream()
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }
}
