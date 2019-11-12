package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getPreInstanticateBeanClasses() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    public Set<Method> getPreInstanticateBeanMethods() {
        Set<Class<?>> typesAnnotatedWith = getTypesAnnotatedWith(Configuration.class);

        return typesAnnotatedWith.stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }
}
