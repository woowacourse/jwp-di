package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

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
            beans.put(clazz, instantiateBean(clazz, injectedConstructor));
        }
    }

    private Object instantiateBean(Class clazz, Constructor<?> constructor) {
        if (constructor == null) {
            return ReflectionUtils.newInstance(clazz);
        }
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = instantiateParameters(parameterTypes);
        return ReflectionUtils.newInstance(constructor, parameters);
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
}
