package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
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
        return beans.containsKey(clazz) ? (T) beans.get(clazz) : instantiate(clazz);
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
        return Arrays.stream(constructor.getParameterTypes())
                .map(type -> BeanFactoryUtils.findConcreteClass(type, preInstantiateBeans))
                .map(this::instantiateBean)
                .toArray(Object[]::new);
    }

    public Map<Class<?>, Object> getBeans(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toMap(identity(), key -> beans.get(key)));
    }
}
