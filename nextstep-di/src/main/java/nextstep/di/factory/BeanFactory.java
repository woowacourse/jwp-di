package nextstep.di.factory;

import nextstep.stereotype.Controller;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Object instantiate(final Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        final Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        final Object bean = Objects.isNull(constructor)
                ? BeanUtils.instantiateClass(clazz)
                : BeanUtils.instantiateClass(constructor, getParameters(constructor));
        beans.put(clazz, bean);
        return bean;
    }

    private Object[] getParameters(final Constructor constructor) {
        final List<Object> parameters = new ArrayList<>();
        final Class[] parameterTypes = constructor.getParameterTypes();
        for (final Class clazz : parameterTypes) {
            final Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            parameters.add(instantiate(cls));
        }
        return parameters.toArray();
    }

    public Map<Class<?>, Object> getControllers() {
        return preInstantiateBeans.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
                .collect(Collectors.toUnmodifiableMap(Function.identity(), beans::get));
    }
}
