package nextstep.di.bean;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathBeanScanner.class);

    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
            Controller.class,
            Service.class,
            Repository.class
    );

    private final Reflections reflections;

    public ClasspathBeanScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> getClasspathBeansToInstantiate() {
        final Set<Class<?>> beans = ANNOTATIONS.stream().flatMap(x ->
            this.reflections.getTypesAnnotatedWith(x).stream()
        ).collect(Collectors.toSet());
        logger.debug("Scanned Bean Types: {}", beans);
        return beans;
    }
}