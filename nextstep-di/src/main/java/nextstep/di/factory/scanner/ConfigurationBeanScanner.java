package nextstep.di.factory.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ConfigurationBeanDefinition;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {

    private final Reflections reflections;

    public ConfigurationBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public Set<BeanDefinition> scan() {
        return getBeanMethods().stream()
                .map(ConfigurationBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    private Set<Method> getBeanMethods() {
        Set<Method> beanMethods = Sets.newHashSet();
        for (Class<?> configClass : scanConfigurationTypes()) {
            beanMethods.addAll(scanBeanMethods(configClass));
        }
        return beanMethods;
    }

    private Set<Class<?>> scanConfigurationTypes() {
        return reflections.getTypesAnnotatedWith(Configuration.class);
    }

    private Set<Method> scanBeanMethods(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
    }
}
