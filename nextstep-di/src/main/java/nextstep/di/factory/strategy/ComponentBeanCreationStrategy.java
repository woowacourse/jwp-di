package nextstep.di.factory.strategy;

import nextstep.di.factory.factory.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentBeanCreationStrategy implements BeanCreationStrategy {
    private final Set<Class<?>> preInstantiateBeans;
    private Set<Class<?>> preComponentInstantiateBeans;

    public ComponentBeanCreationStrategy(Set<Class<?>> preComponentInstantiateBeans) {
        this.preComponentInstantiateBeans = preComponentInstantiateBeans;
        this.preInstantiateBeans = BeanFactoryUtils.getConfigurationBeanClasses(preComponentInstantiateBeans);
        preInstantiateBeans.addAll(preComponentInstantiateBeans);
    }

    @Override
    public boolean canHandle(Class<?> clazz) {
        return preComponentInstantiateBeans.contains(clazz);
    }

    @Override
    public List<Class<?>> getDependencyTypes(Class<?> clazz) {
        clazz = BeanFactoryUtils.findConcreteClass(clazz, preComponentInstantiateBeans);
        Constructor<?> constructor = BeanFactoryUtils.getConstructor(clazz, preComponentInstantiateBeans);
        return Arrays.asList(constructor.getParameterTypes());
    }

    @Override
    public Object createBean(Class<?> clazz, List<Object> parameterInstances, Map<Class<?>, Object> beans) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        clazz = BeanFactoryUtils.findConcreteClass(clazz, preComponentInstantiateBeans);
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Constructor<?> constructor = BeanFactoryUtils.getConstructor(clazz, preComponentInstantiateBeans);
        Object instance = constructor.newInstance(parameterInstances.toArray());

        beans.put(clazz, instance);
        return instance;
    }
}
