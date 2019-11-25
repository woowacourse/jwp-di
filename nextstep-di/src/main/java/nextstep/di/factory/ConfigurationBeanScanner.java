package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.instantiation.MethodInstantiation;
import org.reflections.Reflections;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {

    private final Reflections reflection;

    public ConfigurationBeanScanner(final Object... basePackages) {
        this.reflection = new Reflections(basePackages);
    }

    public BeanCreateMatcher scanBean(BeanCreateMatcher beanCreateMatcher) {
        Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(Configuration.class);
        typesAnnotatedWith.forEach(configurationClazz -> registerMethodBean(beanCreateMatcher, configurationClazz));
        return beanCreateMatcher;
    }

    private void registerMethodBean(BeanCreateMatcher beanCreateMatcher, Class<?> configurationClazz) {
        Set<Method> methods = Arrays.stream(configurationClazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet());
        for (Method method : methods) {
            beanCreateMatcher.put(method.getReturnType(),
                    new MethodInstantiation(method, BeanUtils.instantiateClass(configurationClazz)));
        }
    }
}
