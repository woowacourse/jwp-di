package nextstep.di.utils;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanUtils {
    private BeanUtils() {
    }

    @SuppressWarnings("uncheked")
    public static Set<Class<?>> getTypesAnnotatedWith(Object[] basePackages, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(basePackages);
        return Stream.of(annotations)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
