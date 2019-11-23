package nextstep.di.factory.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ClasspathBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner {
    private final Reflections reflections;
    private final List<Class<? extends Annotation>> annotations;

    public ClasspathBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
        annotations = Arrays.asList(
                Controller.class, Service.class, Repository.class
        );
    }

    public Set<BeanDefinition> scan() {
        return getAnnotatedTypes().stream()
                .map(ClasspathBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    public Set<Class<?>> getAnnotatedTypes() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
