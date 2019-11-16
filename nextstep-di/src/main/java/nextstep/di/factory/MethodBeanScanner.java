package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.InvalidConfigurationClassException;
import nextstep.di.factory.exception.ObjectInstantiationFailException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodBeanScanner implements BeanScanner {

    @Override
    public Map<Class<?>, BeanCreator> scan(Object... basePackages) {
        Set<Class<?>> configClasses = getTypesAnnotatedWith(new Reflections(basePackages), Configuration.class);

        Map<Class<?>, BeanCreator> creators = Maps.newHashMap();

        for (Class<?> configClass : configClasses) {
            creators.putAll(scanSingleConfigClass(configClass));
        }

        return creators;
    }

    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    private Map<Class<?>, BeanCreator> scanSingleConfigClass(Class<?> configClass) {
        validateConfigAnnotated(configClass);

        Object instance;
        try {
            instance = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ObjectInstantiationFailException(e);
        }

        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toMap(Method::getReturnType,
                        (method) -> new MethodBeanCreator(instance, method)));
    }

    private void validateConfigAnnotated(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new InvalidConfigurationClassException();
        }
    }
}
