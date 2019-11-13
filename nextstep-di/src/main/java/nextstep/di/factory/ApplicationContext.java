package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Set;

public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(Object... basePackage) {
        beanFactory = new BeanFactory(generateBeanCreateMatcher(basePackage));
    }

    private BeanCreateMatcher generateBeanCreateMatcher(Object[] basePackage) {
        BeanCreateMatcher beanCreateMatcher = new BeanCreateMatcher();
        BeanScanner beanScanner = new BeanScanner(basePackage);
        beanScanner.scanBean(beanCreateMatcher, Controller.class, Service.class, Repository.class);
        return beanCreateMatcher;
    }

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    public Set<Class<?>> getControllers() {
        return beanFactory.getControllers();
    }
}
