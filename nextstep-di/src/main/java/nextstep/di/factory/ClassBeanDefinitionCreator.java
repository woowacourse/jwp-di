package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ClassBeanDefinitionCreator implements BeanDefinitionCreator {

    @Override
    public Map<Class<?>, BeanDefinition> create(Set<Class<?>> preInstantiateClasses) {
        Map<Class<?>, BeanDefinition> definitions = Maps.newHashMap();

        for (Class<?> clazz : preInstantiateClasses) {
            BeanDefinition definition = createBeanDefinition(clazz);
            definitions.put(clazz, definition);
        }

        return definitions;
    }

    private BeanDefinition createBeanDefinition(Class<?> clazz) {
        Constructor<?> injectedConstructor = createInjectedConstructor(clazz);

        return new BeanDefinition(
                clazz,
                null,
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
}
