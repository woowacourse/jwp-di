package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.InvalidConfigurationClassException;
import nextstep.di.factory.exception.ObjectInstantiationFailException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodBeanScanner {
    public static Map<Class<?>, BeanCreator> scan(Class<?>... configClass) {
        return Arrays.stream(configClass)
                .map(MethodBeanScanner::scanSingleConfigClass)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Map<Class<?>, BeanCreator> scanSingleConfigClass(Class<?> configClass) {
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

    private static void validateConfigAnnotated(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(Configuration.class)) {
            throw new InvalidConfigurationClassException();
        }
    }
}