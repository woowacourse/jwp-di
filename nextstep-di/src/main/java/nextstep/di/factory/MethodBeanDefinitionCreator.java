package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodBeanDefinitionCreator implements BeanDefinitionCreator {

    @Override
    public Map<Class<?>, BeanDefinition> create(Set<Class<?>> preInstantiateClasses) {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();

        preInstantiateClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .forEach(configuration -> saveAllBeanDefinitions(configuration, definitions));

        return definitions;
    }

    private void saveAllBeanDefinitions(Class<?> configuration, Map<Class<?>, BeanDefinition> definitions) {
        List<Method> beanCreators = getBeanCreators(configuration);

        for (Method beanCreator : beanCreators) {
            Class<?> beanType = beanCreator.getReturnType();
            definitions.put(beanType, createBeanDefinition(beanCreator));
        }
    }

    private List<Method> getBeanCreators(Class<?> configuration) {
        Method[] declaredMethods = configuration.getDeclaredMethods();

        return Stream.of(declaredMethods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toList());
    }

    private BeanDefinition createBeanDefinition(Method beanCreator) {
        Class<?> clazz = beanCreator.getReturnType();
        Class<?> configType = beanCreator.getDeclaringClass();

        return new BeanDefinition(
                clazz,
                configType,
                beanCreator::invoke,
                Arrays.asList(beanCreator.getParameterTypes())
        );
    }
}
