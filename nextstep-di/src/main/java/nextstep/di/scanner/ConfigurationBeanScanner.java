package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.definition.BeanDefinition;
import nextstep.di.definition.ConfigurationBeanDefinition;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {

    private final Reflections reflections;

    public ConfigurationBeanScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public Set<BeanDefinition> scan() {
        return getBeanMethods().stream()
                .map(ConfigurationBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    private Set<Method> getBeanMethods() {
        final Set<Method> beanMethods = new HashSet<>();
        for (final Class<?> configClass : scanConfigurationTypes()) {
            beanMethods.addAll(scanBeanMethods(configClass));
        }
        return beanMethods;
    }

    private Set<Class<?>> scanConfigurationTypes() {
        return reflections.getTypesAnnotatedWith(Configuration.class);
    }

    private Set<Method> scanBeanMethods(final Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }

}
