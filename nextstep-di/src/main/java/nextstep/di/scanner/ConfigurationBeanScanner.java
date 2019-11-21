package nextstep.di.scanner;

import nextstep.annotation.Configuration;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.DefaultBeanDefinition;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {

    private static final Class<Configuration> COMPONENT_ANNOTATION = Configuration.class;

    private final List<BeanDefinition> beanDefinitions;

    public ConfigurationBeanScanner(Object... basePackages) {
        Set<Class<?>> classTypes = getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATION);
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
