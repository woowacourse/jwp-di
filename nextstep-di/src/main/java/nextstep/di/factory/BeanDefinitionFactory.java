package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanDefinitionFactory {
    private final Set<Class<?>> preInstantiateClasses;

    public BeanDefinitionFactory(Set<Class<?>> preInstantiateClasses) {
        this.preInstantiateClasses = preInstantiateClasses;
    }

    public Map<Class<?>, BeanDefinition> createBeanDefinitions() {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();

        for (Class<?> clazz : preInstantiateClasses) {
            BeanDefinition definition = createComponentDefinition(clazz);
            definitions.put(clazz, definition);
        }
        preInstantiateClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .forEach(configuration -> definitions.putAll(createBeanDefinitions(configuration)));
        return definitions;
    }

    private BeanDefinition createComponentDefinition(Class<?> clazz) {
        Constructor<?> injectedConstructor = createInjectedConstructor(clazz);

        return new ComponentDefinition(
                clazz,
                (concreteObject, parameters) -> injectedConstructor.newInstance(parameters),
                Arrays.asList(injectedConstructor.getParameterTypes())
        );
    }

    private Constructor<?> createInjectedConstructor(Class concreteClass) {
        Set<Constructor> injectedConstructor = BeanFactoryUtils.getInjectedConstructors(concreteClass);
        return injectedConstructor.isEmpty()
                ? getDefaultConstructor(concreteClass)
                : injectedConstructor.iterator().next();
    }

    @SuppressWarnings("unchecked")
    private Constructor getDefaultConstructor(Class concreteClass) {
        try {
            return concreteClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new BeanCreationFailException(e);
        }
    }

    private Map<Class<?>, BeanDefinition> createBeanDefinitions(Class<?> configuration) {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();
        List<Method> beanCreators = getBeanCreators(configuration);

        for (Method beanCreator : beanCreators) {
            Class<?> beanType = beanCreator.getReturnType();
            definitions.put(beanType, createConfigurationBeanDefinition(beanCreator));
        }
        return definitions;
    }

    private List<Method> getBeanCreators(Class<?> configuration) {
        Method[] declaredMethods = configuration.getDeclaredMethods();

        return Stream.of(declaredMethods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());
    }

    private BeanDefinition createConfigurationBeanDefinition(Method beanCreator) {
        Class<?> clazz = beanCreator.getReturnType();
        Class<?> configType = beanCreator.getDeclaringClass();

        return new ConfigurationBeanDefinition(
                clazz,
                configType,
                beanCreator::invoke,
                Arrays.asList(beanCreator.getParameterTypes())
        );
    }
}
