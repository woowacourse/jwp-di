package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanScanner {
    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};
    private final Set<Class<?>> clazz;
    private final Object[] basePackage;

    public BeanScanner(Object... basePackage) {
        this.basePackage = basePackage;
        this.clazz = getTypesAnnotatedWith(COMPONENT_ANNOTATIONS);
    }

    public Set<Class<?>> getBeans() {
        return clazz;
    }

    @SuppressWarnings("uncheked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(basePackage);
        return Stream.of(annotations)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
