package nextstep.di.factory;

import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.ConstructorBeanDefinition;
import nextstep.stereotype.Component;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathBeanScanner {
    private BeanFactory beanFactory;
    private Set<Class<? extends Annotation>> annotations;

    public ClassPathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        annotations = new HashSet<>(
                Arrays.asList(Component.class, Controller.class, Repository.class, Service.class));
    }

    public void addAnnotationToScan(Class<? extends Annotation>... annotations) {
        this.annotations.addAll(Arrays.asList(annotations));
    }

    public void doScan(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);
            Set<BeanDefinition> definitions = annotatedClasses.stream()
                    .map(ConstructorBeanDefinition::new)
                    .collect(Collectors.toSet());

            beanFactory.registerBeanDefinitions(definitions);
        }
    }
}
