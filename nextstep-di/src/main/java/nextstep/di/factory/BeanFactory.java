package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            beans.put(preInstanticateBean, createBean(preInstanticateBean));
        }
    }

    private Object createBean(Class<?> preInstanticateBean) {
        try {
            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);
            if (injectedConstructor == null) {
                return createDefaultConstructorInstance(preInstanticateBean);
            }
            return createInstance(injectedConstructor);
        } catch (Exception e) {
            logger.error("### Bean create fail : {}", e.getMessage());
            throw new IllegalArgumentException("Bean create fail!");
        }
    }

    private Object createDefaultConstructorInstance(Class<?> preInstanticateBean) throws IllegalAccessException, InstantiationException {
        return preInstanticateBean.newInstance();
    }

    private Object createInstance(Constructor<?> injectedConstructor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameters.add(getBean(parameterType));
        }
        return injectedConstructor.newInstance(parameters.toArray());
    }
}
