package nextstep.di.scanner;

import org.reflections.Reflections;
import org.slf4j.Logger;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class BeanScanner {
    private static final Logger LOG = getLogger(BeanScanner.class);

    private final Reflections reflections;

    public BeanScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    @SuppressWarnings("unchecked")
    protected Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation>... annotations) {
        final Set<Class<?>> beans = new HashSet<>();
        for (final Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        LOG.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    public abstract Set<Class<?>> getBeans();
}
