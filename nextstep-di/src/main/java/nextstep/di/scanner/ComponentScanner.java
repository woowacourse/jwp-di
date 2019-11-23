package nextstep.di.scanner;

import nextstep.di.factory.BeanDefinition;
import nextstep.di.factory.ClassBeanDefinition;
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
    public Set<BeanDefinition> getBeanDefinitions() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class).stream()
                .map(ClassBeanDefinition::of)
                .collect(Collectors.toSet());
    }
}
