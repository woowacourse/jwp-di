package nextstep.di.factory.beans;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);
    private Reflections reflections;

    public ConfigurationBeanScanner(Class<?> initPoint) {
        this.reflections = new Reflections(initPoint.getPackage().getName());
    }

    @Override
    public Set<BeanRecipe> scan() {
        Map<Class<?>, Object> instances = new HashMap<>();
        return reflections.getTypesAnnotatedWith(Configuration.class).stream()
                .peek(putInstances(instances))
                .flatMap(this::getAnnotatedMethods)
                .map(method -> new MethodBeanRecipe(method, instances.get(method.getDeclaringClass())))
                .collect(Collectors.toSet());
    }

    private Stream<Method> getAnnotatedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class));
    }

    private Consumer<Class<?>> putInstances(Map<Class<?>, Object> instances) {
        return clazz -> {
            if (instances.containsKey(clazz)) {
                return;
            }
            try {
                instances.put(clazz, clazz.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                log.error(e.getMessage());
                throw new IllegalBeanException(e);
            }
        };
    }
}
