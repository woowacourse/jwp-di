package nextstep.di.scanner;

import com.google.common.collect.Sets;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public abstract class BeanScannerImpl implements BeanScanner {
    private Reflections reflections;

    BeanScannerImpl(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    Set<Class<?>> scanAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
            beans.addAll(typesAnnotatedWith);
        }
        return beans;
    }
}
