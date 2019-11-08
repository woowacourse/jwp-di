package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

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
        for (Class clazz : preInstantiateBeans) {
            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
            putInstance(clazz, injectedConstructor);
        }
    }

    private void putInstance(Class clazz, Constructor<?> injectedConstructor) {
        if (hasNotClass(clazz)) {
            logger.debug("class name : {}, annotation inject constructor: {}", clazz.getName(), injectedConstructor);
            beans.put(clazz, instantiateBean(clazz, injectedConstructor));
        }
    }

    private boolean hasNotClass(Class clazz) {
        return !beans.containsKey(clazz);
    }

    private Object instantiateBean(Class<?> clazz, Constructor<?> constructor) {
        if (constructor == null) {
            return getBeanOrDefault(clazz);
        }
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = instantiateParameters(parameterTypes);
        return getBeanOrDefault(constructor, parameters);
    }

    private Object getBeanOrDefault(Constructor<?> constructor, Object[] parameters) {
        return putIfAbsent(constructor.getDeclaringClass(), ReflectionUtils.newInstance(constructor, parameters));
    }

    private Object getBeanOrDefault(Class<?> clazz) {
        return putIfAbsent(clazz, ReflectionUtils.newInstance(clazz));
    }

    private Object putIfAbsent(Class<?> clazz, Object instance) {
        Object bean = beans.getOrDefault(clazz, instance);
        beans.putIfAbsent(bean.getClass(), bean);
        return bean;
    }

    private Object[] instantiateParameters(Class<?>[] parameterTypes) {
        Object[] instances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            instances[i] = instantiateInjectedConstructor(parameterTypes[i]);
        }
        return instances;
    }

    private Object instantiateInjectedConstructor(Class parameterType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(parameterType);
        return instantiateBean(concreteClass, injectedConstructor);
    }

    public Map<Class<?>, Object> getBeans(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toMap(identity(), key -> beans.get(key)));
    }
}
