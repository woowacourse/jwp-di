package nextstep.di.factory.scanner;

import nextstep.di.factory.beans.BeanRecipe;
import nextstep.di.factory.beans.ConstructorBeanRecipe;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ComponentBeanScanner.class);
    private static final Set<Class<? extends Annotation>> TARGET_ANNOTATIONS = new HashSet<>(Arrays.asList(
            Controller.class, Service.class, Repository.class)
    );

    private Reflections reflections;

    public ComponentBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public Set<BeanRecipe> scan() {
        return TARGET_ANNOTATIONS.stream()
                .map(type -> reflections.getTypesAnnotatedWith(type))
                .flatMap(Collection::stream)
                .map(ConstructorBeanRecipe::new)
                .collect(Collectors.toSet());
    }
}
