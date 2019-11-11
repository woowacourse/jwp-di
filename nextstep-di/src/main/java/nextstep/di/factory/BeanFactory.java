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
            instantiateBean(clazz);
        }
    }

    private Object instantiateBean(Class<?> clazz) {
        logger.debug("getSingletonInstance()...");
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Object instance = createInstance(clazz);
        beans.put(clazz, instance);
        return instance;
    }

    private Object createInstance(Class<?> clazz) {
        logger.debug("after getInstance()...");
        logger.debug("Class Type : {}", clazz);

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (injectedConstructor == null) {
            return createBean(clazz);
        }

        Class<?> parameterTypes[] = injectedConstructor.getParameterTypes();
        Object[] instances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = BeanFactoryUtils.findConcreteClass(parameterTypes[i], preInstantiateBeans);
            instances[i] = instantiateBean(parameterType);
        }
        return createBeanWithParameters(injectedConstructor, instances);
    }

    private Object createBean(Class<?> clazz) {
        return ReflectionUtils.newInstance(clazz);
    }

    private Object createBeanWithParameters(Constructor<?> injectedConstructor, Object[] instances) {
        return ReflectionUtils.newInstance(injectedConstructor, instances);
    }
}

