package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class BeanFactory {
    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> clazz : preInstantiateBeans) {
            beans.putIfAbsent(clazz, instantiateBean(clazz));
        }
    }

    private <T> T instantiateBean(Class<T> clazz) {
        if (beans.containsKey(clazz)) {
            return (T) beans.get(clazz);
        }
        return instantiate(clazz);
    }

    private <T> T instantiate(Class<T> clazz) {
        Constructor<T> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (constructor == null) {
            return ReflectionUtils.newInstance(clazz);
        }
        Object[] parameters = instantiateParameters(constructor);
        return ReflectionUtils.newInstance(constructor, parameters);
    }

    private Object[] instantiateParameters(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameters.length; i++) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterTypes[i], preInstantiateBeans);
            parameters[i] = instantiateBean(concreteClass);
        }
        return parameters;
    }

    public Map<Class<?>, Object> getBeans(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toMap(identity(), key -> beans.get(key)));
    }
}
