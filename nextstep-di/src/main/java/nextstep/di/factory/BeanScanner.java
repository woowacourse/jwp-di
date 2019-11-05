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
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);

    private final Reflections reflections;

    public BeanScanner(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getPreInstantiateBeans() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(this.reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scanned Bean Types: {}", beans);
        return beans;
    }
}