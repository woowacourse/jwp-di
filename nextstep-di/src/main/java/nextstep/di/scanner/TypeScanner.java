package nextstep.di.scanner;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class TypeScanner {
    private static final Logger log = LoggerFactory.getLogger(TypeScanner.class);

    private static final String[] COMPONENT_PACKAGE_PATHS = {"nextstep.annotation"};

    private Object[] basePackages;
    private Reflections reflections;

    public TypeScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages, COMPONENT_PACKAGE_PATHS);

        this.basePackages = basePackages;
    }

    public Set<Class<?>> scanAnnotatedWith(Class<? extends Annotation>... annotations) {
        this.reflections = new Reflections(basePackages, COMPONENT_PACKAGE_PATHS, annotations);

        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation).stream()
                    .filter(type -> !type.isInterface())
                    .filter(type -> !type.isAnnotation())
                    .collect(Collectors.toSet());

            log.debug("annotation: {}, beans : {}", annotation, typesAnnotatedWith);

            beans.addAll(typesAnnotatedWith);
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
