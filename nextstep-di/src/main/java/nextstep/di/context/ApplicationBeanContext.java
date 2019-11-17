package nextstep.di.context;

import nextstep.di.factory.AnnotatedBeanFactory;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.BeanRegistry;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.BeanScanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ApplicationBeanContext implements BeanFactory {

    private BeanRegistry beanRegistry = new BeanRegistry();
    private Object[] root;

    public ApplicationBeanContext(Object... root) {
        this.root = root;
        initialize();
    }

    @Override
    public void initialize() {
        BeanScanner beanScanner = new AnnotatedBeanScanner(this, Controller.class, Service.class, Repository.class);
        BeanFactory beanFactory = new AnnotatedBeanFactory(beanRegistry, beanScanner);

        beanScanner.doScan();
        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanRegistry.get(requiredType);
    }

    @Override
    public Set<Class<?>> getTypes(Class<? extends Annotation> annotation) {
        return new AnnotatedBeanScanner(this, annotation).doScan();
    }

    public Object[] getRoot() {
        return root;
    }
}
