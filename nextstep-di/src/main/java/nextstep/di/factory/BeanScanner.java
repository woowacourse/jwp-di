package nextstep.di.factory;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private Class<? extends Annotation>[] types;


    public BeanScanner(Object[] basePackage, Class<? extends Annotation>... types) {
        reflections = new Reflections(basePackage);
        this.types = types;
    }

    public BeanFactory getBeanFactory() {
        return new BeanFactory(scanBeans());
    }

    private Set<Class<?>> scanBeans() {
        return Arrays.stream(types)
                .map(type -> reflections.getTypesAnnotatedWith(type))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
