package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
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
            if (injectedConstructor != null) {
                initiateBeans(clazz, injectedConstructor);
                continue;
            }
            beans.put(clazz, ReflectionUtils.newInstance(clazz));
        }
    }

    private void initiateBeans(Class clazz, Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        try {
            Object[] constructors = instantiateParameters(parameterTypes);
            beans.put(clazz, constructor.newInstance(constructors));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error(e.getMessage(), e);
        }
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

        if (injectedConstructor == null) {
            return ReflectionUtils.newInstance(concreteClass);
        }
        return ReflectionUtils.newInstance(
                injectedConstructor,
                instantiateParameters(injectedConstructor.getParameterTypes()));
    }
}
