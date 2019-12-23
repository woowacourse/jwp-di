package nextstep.di.beandefinition;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanDefinitionRegister {
    public static BeanDefinitionRegistry register(Set<Class<?>> scannedTypes) {
        BeanDefinitionRegistry registry = BeanDefinitionRegistry.create();

        registerFromTypes(scannedTypes, registry);

        registerBeanMethodsFromConfigTypes(collectConfigTypes(scannedTypes), registry);

        return registry;
    }

    private static void registerFromTypes(Set<Class<?>> scannedTypes, BeanDefinitionRegistry registry) {
        for (Class<?> type : scannedTypes) {
            registry.register(TypeBeanDefinition.of(type));
        }
    }

    private static Set<Class<?>> collectConfigTypes(Set<Class<?>> scannedTypes) {
        return scannedTypes.stream()
                .filter(type -> type.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());
    }

    private static void registerBeanMethodsFromConfigTypes(Set<Class<?>> configTypes, BeanDefinitionRegistry registry) {
        for (Class<?> configType : configTypes) {
            registerBeanMethods(configType, registry);
        }
    }

    private static void registerBeanMethods(Class<?> configType, BeanDefinitionRegistry registry) {
        List<Method> beanMethods = Arrays.asList(configType.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());

        beanMethods.stream()
                .map(method -> MethodBeanDefinition.of(method))
                .forEach(definition -> registry.register(definition));
    }
}
