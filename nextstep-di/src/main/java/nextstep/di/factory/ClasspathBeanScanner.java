package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Set;

public class ClasspathBeanScanner implements BeanScanner {
    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};

    private final Set<Class<?>> classTypes;

    public ClasspathBeanScanner(Object... basePackage) {
        this.classTypes = getTypesAnnotatedWith(basePackage, COMPONENT_ANNOTATIONS);
    }

    @Override
    public Set<Class<?>> getClassTypes() {
        return classTypes;
    }
}
