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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);
    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(Controller.class, Service.class, Repository.class);

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getTypesAnnotated() {
        return ANNOTATIONS.stream()
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .peek(bean -> logger.debug("Scan Beans Type : {}", bean))
                .collect(Collectors.toSet());
    }
}
