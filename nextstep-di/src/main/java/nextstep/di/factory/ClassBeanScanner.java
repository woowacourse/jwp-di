package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassBeanScanner implements BeanScanner {

    @Override
    public Map<Class<?>, BeanCreator> scan(Object... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> types = getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class, Configuration.class);
        types.addAll(getTypesFromComponentScan(types));
        return Maps.asMap(types, ClassBeanCreator::new);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    private Set<Class<?>> getTypesFromComponentScan(Set<Class<?>> types) {
        return types.stream()
                .filter(type -> type.isAnnotationPresent(ComponentScan.class))
                .map(type -> type.getAnnotation(ComponentScan.class).basePackages())
                .map(packages -> getTypesAnnotatedWith(new Reflections(packages, Controller.class, Service.class, Repository.class)))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
