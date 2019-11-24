package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ConfigurationBeanDefinition;
import nextstep.di.factory.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ConfigurationBeanDefinitionScanner implements BeanDefinitionScanner {
    private static final Set<Class<? extends Annotation>> DEFAULT_ANNOTATION =
            Stream.of(Configuration.class).collect(toSet());

    private final Set<Class<?>> classes;

    public ConfigurationBeanDefinitionScanner(Set<String> packages) {
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
                .map(this::scanConfigurationClass)
                .flatMap(Collection::stream)
                .collect(toSet()));
    }

    private Set<BeanDefinition> scanConfigurationClass(Class<?> clazz) {
        Object instance = ReflectionUtils.newInstance(clazz);
        return Stream.of(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new ConfigurationBeanDefinition(instance, method))
                .collect(toSet());
    }
}
