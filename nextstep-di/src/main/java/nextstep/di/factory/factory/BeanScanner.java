package nextstep.di.factory.factory;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);
    private Reflections reflections;

    public BeanScanner(Object... basePackages) {
        reflections = new Reflections(basePackages);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
