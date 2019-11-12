package nextstep.di.scanner;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);
    private static final Set<Class<? extends Annotation>> DEFAULT_ANNOTATION =
            Stream.of(Controller.class, Service.class, Repository.class)
                    .collect(toSet());

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> scanDefaultAnnotation() {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : DEFAULT_ANNOTATION) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
