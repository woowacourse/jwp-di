package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getPreInstanticateClass() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotation);
            beans.addAll(annotated);
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
