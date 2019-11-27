package nextstep.di.scanner;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class TypeScanner {
    private static final Logger log = LoggerFactory.getLogger(TypeScanner.class);
    private Reflections reflections;

    public TypeScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> scanAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
            log.debug("annotation: {}, beans : {}", annotation, typesAnnotatedWith);
            beans.addAll(typesAnnotatedWith);
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
