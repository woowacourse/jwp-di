package nextstep.di.factory.context;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.beans.ComponentBeanScanner;
import nextstep.di.factory.beans.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?> entryPoint) {
        beanFactory = new BeanFactory();
        beanFactory.addScanner(new ConfigurationBeanScanner(entryPoint));
        ComponentScan componentScan = entryPoint.getAnnotation(ComponentScan.class);
        beanFactory.addScanner(new ComponentBeanScanner(componentScan.basePackages()));
    }

    public void initialize() {
        beanFactory.initialize();
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }
}
