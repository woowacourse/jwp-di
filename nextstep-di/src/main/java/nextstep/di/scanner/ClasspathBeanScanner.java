package nextstep.di.scanner;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Set;

public class ClasspathBeanScanner extends BeanScanner {
    private final Set<Class<?>> preInstantiateBeans;

    @SuppressWarnings("unchecked")
    public ClasspathBeanScanner(final Object... basePackage) {
        super(basePackage);
        preInstantiateBeans = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @Override
    public Set<Class<?>> getBeans() {
        return preInstantiateBeans;
    }
}
