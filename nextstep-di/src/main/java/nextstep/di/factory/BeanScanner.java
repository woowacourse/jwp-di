package nextstep.di.factory;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);

    private final Reflections reflections;

    public BeanScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> getPreInstantiateBeans() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        final Set<Class<?>> beans = Stream.of(annotations).flatMap(x ->
                this.reflections.getTypesAnnotatedWith(x).stream()
        ).collect(Collectors.toSet());
        logger.debug("Scanned Bean Types: {}", beans);
        return beans;
    }
}