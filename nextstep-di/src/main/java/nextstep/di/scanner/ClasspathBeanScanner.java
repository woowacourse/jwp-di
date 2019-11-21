package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.DefaultBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClasspathBeanScanner implements BeanScanner {

    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};

    private final List<BeanDefinition> beanDefinitions;

    public ClasspathBeanScanner(Object... basePackages) {
        Set<Class<?>> classTypes = getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATIONS);
        this.beanDefinitions = initBeanDefinitions(classTypes);
    }

    @SuppressWarnings("uncheked")
    private Set<Class<?>> getTypesAnnotatedWith(Object[] basePackages, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(basePackages);
        return Stream.of(annotations)
                .map(reflections::getTypesAnnotatedWith)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private List<BeanDefinition> initBeanDefinitions(Set<Class<?>> classTypes) {
        return classTypes.stream()
                .map(DefaultBeanDefinition::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableList(beanDefinitions);
    }
}
