package nextstep.di.factory;

import nextstep.stereotype.Controller;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {
    private final Set<Class<?>> preInstantiateBeans;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(final Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
        initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    private void initialize() {
        preInstantiateBeans.forEach(this::instantiate);
    }

    @SuppressWarnings("unchecked")
    private Object instantiate(final Class<?> aClass) {
        if (beans.containsKey(aClass)) {
            return beans.get(aClass);
        }
        final Constructor constructor = BeanFactoryUtils.getInjectedConstructor(aClass);
        final Object bean = Objects.isNull(constructor)
                ? BeanUtils.instantiateClass(aClass)
                : BeanUtils.instantiateClass(constructor, getArguments(constructor).toArray());
        beans.put(aClass, bean);
        return bean;
    }

    private List<Object> getArguments(final Constructor constructor) {
        final List<Object> arguments = new ArrayList<>();
        final Class[] parameterTypes = constructor.getParameterTypes();
        for (final Class clazz : parameterTypes) {
            final Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            arguments.add(instantiate(cls));
        }
        return arguments;
    }

    public Map<Class<?>, Object> getControllers() {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }
}
