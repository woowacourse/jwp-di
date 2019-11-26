package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(Object... basePackages) {
        Object[] customPath = findCustomPath(basePackages);
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        createBeanCreateMatcher(beanCreateMatcher, customPath);

        this.beanFactory = new BeanFactory(beanCreateMatcher);
    }

    private void createBeanCreateMatcher(BeanCreateMatcher beanCreateMatcher, Object[] customPath) {
        AnnotationBeanScanner annotationBeanScanner = new AnnotationBeanScanner(customPath);
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(customPath);
        annotationBeanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        configurationBeanScanner.scanBean(beanCreateMatcher);
    }

    private Object[] findCustomPath(Object[] basePackages) {
        Object[] customPackages = Arrays.stream(basePackages)
                .filter(object -> object instanceof Class)
                .map(clazz -> (((Class) clazz).getAnnotation(ComponentScan.class)))
                .map(annotation -> ((ComponentScan) annotation).basePackages())
                .toArray();

        return customPackages.length == 0 ? basePackages : customPackages;
    }

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getControllers() {
        return beanFactory.getControllers();
    }
}
