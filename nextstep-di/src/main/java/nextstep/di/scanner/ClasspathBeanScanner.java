package nextstep.di.scanner;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class ClasspathBeanScanner implements BeanScanner {
    private static final Logger LOG = getLogger(ClasspathBeanScanner.class);

    private final Reflections reflections;
    private final Set<Class<?>> preInstantiateBeans;

    @SuppressWarnings("unchecked")
    public ClasspathBeanScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
        preInstantiateBeans = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation>... annotations) {
        final Set<Class<?>> beans = new HashSet<>();
        for (final Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        LOG.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    @Override
    public Set<Class<?>> getBeans() {
        return preInstantiateBeans;
    }
}
