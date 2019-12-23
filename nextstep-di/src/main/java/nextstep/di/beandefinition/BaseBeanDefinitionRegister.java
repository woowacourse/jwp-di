package nextstep.di.beandefinition;

import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class BaseBeanDefinitionRegister implements BeanDefinitionRegister {

    private final TypeBeanDefinitionRegister typeRegister;
    private final ConfigBeanDefinitionRegister configRegister;

    public BaseBeanDefinitionRegister(TypeBeanDefinitionRegister typeRegister, ConfigBeanDefinitionRegister configRegister) {
        this.typeRegister = typeRegister;
        this.configRegister = configRegister;
    }

//    public static BaseBeanDefinitionRegister of(BeanDefinitionRegistry registry) {
//        return new BaseBeanDefinitionRegister(TypeBeanDefinitionRegister.of(registry), ConfigBeanDefinitionRegister.of(registry));
//    }

    public static BaseBeanDefinitionRegister from(BeanDefinitionRegistry registry, BeanFactory beanFactory) {
        return new BaseBeanDefinitionRegister(TypeBeanDefinitionRegister.of(registry), ConfigBeanDefinitionRegister.from(registry, beanFactory));
    }

    @Override
    public BeanDefinitionRegistry register(Set<Class<?>> scannedTypes) {
        Set<Class<?>> notConfigTypes = scannedTypes.stream()
                .filter(type -> !type.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());

        Set<Class<?>> configTypes = scannedTypes.stream()
                .filter(type -> type.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());

        return BeanDefinitionRegistry.merge(
                typeRegister.register(notConfigTypes),
                configRegister.register(configTypes));
    }
}
