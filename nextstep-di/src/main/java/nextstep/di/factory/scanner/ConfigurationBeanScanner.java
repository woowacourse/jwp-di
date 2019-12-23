package nextstep.di.factory.scanner;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.ConfigurationDefinition;
import nextstep.exception.ConfigurationCreateException;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {
    private final Reflections reflections;

    public ConfigurationBeanScanner(Class<?> configureClass) {
        this.reflections = new Reflections(configureClass.getPackage().getName());
    }

    public List<ComponentScan> findComponentScans() {
        return reflections.getTypesAnnotatedWith(ComponentScan.class)
            .stream()
            .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
            .map(clazz -> clazz.getAnnotation(ComponentScan.class))
            .collect(Collectors.toList());
    }

    @Override
    public Set<BeanDefinition> scan() {
        Map<Class<?>, Object> instances = Maps.newHashMap();
        return reflections.getTypesAnnotatedWith(Configuration.class)
            .stream()
            .peek(putInstances(instances))
            .flatMap(this::getBeanAnnotatedMethods)
            .map(method -> new ConfigurationDefinition(method, instances.get(method.getDeclaringClass())))
            .collect(Collectors.toSet());
    }

    private Stream<Method> getBeanAnnotatedMethods(Class<?> clazz) {
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
                e.printStackTrace();
                throw new ConfigurationCreateException("Configuration 클래스를 생성할 수 없습니다.", e);
            }
        };
    }
}
