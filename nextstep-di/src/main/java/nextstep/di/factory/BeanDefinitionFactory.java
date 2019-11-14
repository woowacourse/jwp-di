package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanDefinitionFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanDefinitionFactory.class);

    private final Set<Class<?>> preInstantiateClasses;

    public BeanDefinitionFactory(Set<Class<?>> preInstantiateClasses) {
        this.preInstantiateClasses = preInstantiateClasses;
    }

    public Map<Class<?>, BeanDefinition> createBeanDefinition() {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();

        for (Class<?> preInstantiateClass : preInstantiateClasses) {
            Constructor<?> injectedConstructor = createInjectedConstructor(preInstantiateClass);
            definitions.put(preInstantiateClass, createBeanDefinition(preInstantiateClass, injectedConstructor));
            createBeanDefinitionsOfConfiguration(definitions, preInstantiateClass);
        }

        return definitions;
    }

    private Constructor<?> createInjectedConstructor(Class concreteClass) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        return injectedConstructor == null ? getDefaultConstructor(concreteClass) : injectedConstructor;
    }

    @SuppressWarnings("unchecked")
    private Constructor getDefaultConstructor(Class concreteClass) {
        try {
            return concreteClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

    // TODO: 19. 11. 14. 메서드 매개변수로 선언된 definitions 리팩토링
    private void createBeanDefinitionsOfConfiguration(Map<Class<?>, BeanDefinition> definitions, Class<?> clazz) {
        if (clazz.isAnnotationPresent(Configuration.class)) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            List<Method> beanCreators = getBeanCreators(declaredMethods);
            addBeanDefinition(definitions, beanCreators);
        }
    }

    private List<Method> getBeanCreators(Method[] declaredMethods) {
        return Stream.of(declaredMethods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());
    }

    private void addBeanDefinition(Map<Class<?>, BeanDefinition> definitions, List<Method> beanCreations) {
        for (Method beanCreation : beanCreations) {
            Class<?> beanType = beanCreation.getReturnType();
            Class<?> configType = beanCreation.getDeclaringClass();
            definitions.put(beanType, createBeanDefinition(beanType, configType, beanCreation));
        }
    }

    private BeanDefinition createBeanDefinition(Class<?> clazz, Constructor<?> injectedConstructor) {
        return new BeanDefinition(
                clazz,
                null,
                (concreteObject, parameters) -> injectedConstructor.newInstance(parameters),
                Arrays.asList(injectedConstructor.getParameterTypes())
        );
    }

    private BeanDefinition createBeanDefinition(Class<?> clazz, Class<?> configClass, Method beanCreation) {
        return new BeanDefinition(
                clazz,
                configClass,
                beanCreation::invoke,
                Arrays.asList(beanCreation.getParameterTypes())
        );
    }
}
