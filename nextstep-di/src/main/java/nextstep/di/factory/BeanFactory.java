package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Objects;
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
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            scanBean(preInstantiateBean);
        }
    }

    private Object scanBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return beans.get(preInstantiateBean);
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (Objects.isNull(injectedConstructor)) {
            Object instance = BeanUtils.instantiateClass(preInstantiateBean);
            beans.put(preInstantiateBean, instance);
            logger.debug("bean name : {}, instance : {}", preInstantiateBean, instance);
            return instance;
        }

        return putParameterizedObject(preInstantiateBean, injectedConstructor);
    }

    private Object putParameterizedObject(Class<?> preInstantiateBean, Constructor<?> constructor) {
        Object[] params = getConstructorParams(constructor);
        Object instance = BeanUtils.instantiateClass(constructor, params);
        beans.put(preInstantiateBean, instance);
        logger.debug("bean name : {}, instance : {}", preInstantiateBean, instance);
        return instance;
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        Object[] params = new Object[constructor.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = constructor.getParameterTypes()[i];
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
            params[i] = scanBean(concreteClass);
        }
        return params;
    }
}

