package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Set;

public class ClasspathBeanScanner implements BeanScanner {

    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};

    private final Set<Class<?>> classTypes;
    private final Object[] basePackages;

    public ClasspathBeanScanner(Object... basePackages) {
        this.basePackages = basePackages;
        this.classTypes = getTypesAnnotatedWith(COMPONENT_ANNOTATIONS);
    }

    public Set<Class<?>> getBeans() {
        return classTypes;
    }

    @Override
    public Set<Class<?>> getClassTypes() {
        return classTypes;
    }
}
