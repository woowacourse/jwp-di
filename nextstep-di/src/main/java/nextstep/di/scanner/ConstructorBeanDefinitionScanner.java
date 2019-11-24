package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ConstructorBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ConstructorBeanDefinitionScanner implements BeanDefinitionScanner {
    private static final Set<Class<? extends Annotation>> DEFAULT_ANNOTATION =
            Stream.of(Controller.class, Service.class, Repository.class).collect(toSet());

    private final Set<Class<?>> classes;

    public ConstructorBeanDefinitionScanner(Set<String> packages) {
        this.classes = getAnnotatedClasses(new Reflections(packages));
    }

    private Set<Class<?>> getAnnotatedClasses(Reflections reflections) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : DEFAULT_ANNOTATION) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @Override
    public Set<BeanDefinition> scan() {
        return Collections.unmodifiableSet(classes.stream()
                .map(ConstructorBeanDefinition::new)
                .collect(toSet()));
    }
}
