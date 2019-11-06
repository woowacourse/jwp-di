package nextstep.di.scanner;

import nextstep.di.factory.BeanConstructor;
import nextstep.di.factory.ClassBeanConstructor;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner extends BeanScanner {

    public ComponentScanner(Object... basePackage) {
        super(basePackage);
    }

    @Override
    public Set<BeanConstructor> getBeanConstructors() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class).stream()
                .map(ClassBeanConstructor::of)
                .collect(Collectors.toSet());
    }
}
