package nextstep.di.bean;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);

    private final Reflections reflections;

    public BeanScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> getClasspathBeansToInstantiate() {
        final Set<Class<?>> beans = getTypesAnnotatedAs(Controller.class, Service.class, Repository.class);
        logger.debug("Scanned Bean Types: {}", beans);
        return beans;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedAs(Class<? extends Annotation>... annotations) {
        return Stream.of(annotations).flatMap(x ->
            this.reflections.getTypesAnnotatedWith(x).stream()
        ).collect(Collectors.toSet());
    }

    public Map<Class<?>, Method> getConfigBeansToInstantiate() {
        final Map<Class<?>, Method> beans = this.reflections.getTypesAnnotatedWith(
                Configuration.class
        ).stream()
        .map(Class::getDeclaredMethods)
        .flatMap(Stream::of)
        .filter(f -> f.isAnnotationPresent(Bean.class))
        .collect(Collectors.toMap(Method::getReturnType, Function.identity()));
        logger.debug("Scanned Bean Types: {}", beans.keySet());
        return beans;
    }
}