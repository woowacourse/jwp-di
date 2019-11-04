package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private Set<Class<?>> preInstantiateBeans;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
        preInstantiateBeans = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        return Arrays.stream(annotations)
                .map(annotation -> reflections.getTypesAnnotatedWith(annotation))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getBeans() {
        return preInstantiateBeans;
    }

}
