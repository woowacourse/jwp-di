package nextstep.di.beandefinition;

import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class TypeBeanDefinitionRegister implements BeanDefinitionRegister {
    private static final Logger log = LoggerFactory.getLogger(TypeBeanDefinitionRegister.class);

    private final BeanDefinitionRegistry registry;

    private TypeBeanDefinitionRegister(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public static TypeBeanDefinitionRegister of(BeanDefinitionRegistry registry) {
        return new TypeBeanDefinitionRegister(registry);
    }

    @Override
    public BeanDefinitionRegistry register(Set<Class<?>> scannedTypes) {
        registerFromTypes(scannedTypes);

        return registry;
    }

    private void registerFromTypes(Set<Class<?>> scannedTypes) {
        for (Class<?> type : scannedTypes) {
            registry.register(TypeBeanDefinition.of(type));
        }
    }

    private Set<Class<?>> collectConfigTypes(Set<Class<?>> scannedTypes) {
        return scannedTypes.stream()
                .filter(type -> type.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());
    }
}
