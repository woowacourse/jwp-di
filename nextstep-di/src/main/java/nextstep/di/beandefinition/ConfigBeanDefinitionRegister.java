package nextstep.di.beandefinition;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigBeanDefinitionRegister implements BeanDefinitionRegister {
    private static final Logger log = LoggerFactory.getLogger(ConfigBeanDefinitionRegister.class);

    private final BeanDefinitionRegistry registry;

    private BeanFactory beanFactory;

    private ConfigBeanDefinitionRegister(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public ConfigBeanDefinitionRegister(BeanDefinitionRegistry registry, BeanFactory beanFactory) {
        this.registry = registry;
        this.beanFactory = beanFactory;
    }

    public static ConfigBeanDefinitionRegister of(BeanDefinitionRegistry registry) {
        return new ConfigBeanDefinitionRegister(registry);
    }

    public static ConfigBeanDefinitionRegister from(BeanDefinitionRegistry registry, BeanFactory beanFactory) {
        return new ConfigBeanDefinitionRegister(registry, beanFactory);
    }

    @Override
    public BeanDefinitionRegistry register(Set<Class<?>> configTypes) {
        validate(configTypes);

        List<Class<?>> originConfigTypes = new ArrayList(configTypes);
        List<Class<?>> proxiedConfigTypes = originConfigTypes.stream()
                .map(configType -> adjustProxyToConfiguration(configType))
                .collect(Collectors.toList());

        registerFromTypes(proxiedConfigTypes);
        registerBeanMethodsFromConfigTypes(originConfigTypes, proxiedConfigTypes);

        return registry;
    }

    private void validate(Set<Class<?>> configTypes) {
        configTypes.stream()
                .forEach(configType -> validate(configType));
    }

    private void validate(Class<?> configType) {
        if (!configType.isAnnotationPresent(Configuration.class)) {
            String s = String.format("@Configuration 이 달린 타입만 사용가능 합니다. type: %d", configType);
            throw new IllegalStateException(s);
        }
    }

    private Class<?> adjustProxyToConfiguration(Class<?> configType) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(configType);
        enhancer.setCallbackType(BeanFactory.BeanMethodInterceptor.class);

        Class<?> proxiedConfigType = enhancer.createClass();
        Enhancer.registerCallbacks(proxiedConfigType, beanFactory.callbacks);

        return proxiedConfigType;
    }

    private void registerFromTypes(List<Class<?>> scannedTypes) {
        for (Class<?> type : scannedTypes) {
            registry.register(TypeBeanDefinition.of(type));
        }
    }

    private void registerBeanMethodsFromConfigTypes(List<Class<?>> originConfigTypes, List<Class<?>> proxiedConfigTypes) {
        log.debug("originConfigTypes: {}", originConfigTypes);

        for (int i = 0; i < originConfigTypes.size(); i++) {
            registerBeanMethods(originConfigTypes.get(i), proxiedConfigTypes.get(i));
        }
    }

    private void registerBeanMethods(Class<?> originConfigType, Class<?> proxiedConfigType) {
        List<Method> beanMethods = Arrays.asList(originConfigType.getDeclaredMethods()).stream()
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());

        log.debug("beanMethods: {}", beanMethods);

        beanMethods.stream()
                .map(method -> getMethodBeanDefinition(proxiedConfigType, method))
                .forEach(definition -> registry.register(definition));
    }

    private MethodBeanDefinition getMethodBeanDefinition(Class<?> proxiedConfigType, Method method) {
        try {
            return MethodBeanDefinition.of(proxiedConfigType.getDeclaredMethod(method.getName(), method.getParameterTypes()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
