package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.definition.BeanDefinition;
import nextstep.exception.BeanInstantiationException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public void register(Set<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            this.beanDefinitions.put(beanDefinition.getClassType(), beanDefinition);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            putBean(clazz);
        }
    }

    private Object putBean(Class<?> clazz) {
        beans.put(clazz, instantiate(clazz));
        return beans.get(clazz);
    }

    private Object instantiate(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        try {
            return createInstance(beanDefinitions.get(BeanFactoryUtils.findConcreteClass(clazz, beanDefinitions.keySet())));
        } catch (Exception e) {
            logger.debug("Fail to instantiate bean {}", e.getMessage());
            throw new BeanInstantiationException();
        }
    }

    private Object createInstance(BeanDefinition beanDefinition) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        if (Objects.isNull(parameterTypes)) {
            return beanDefinition.instantiate();
        }
        return beanDefinition.instantiate(instantiate(parameterTypes));
    }

    private Object[] instantiate(Class<?>[] parameterTypes) {
        if (Objects.isNull(parameterTypes)) {
            return null;
        }

        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameters.add(putBean(parameterType));
        }
        return parameters.toArray();
    }

    public Set<Class<?>> getController() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(toSet());
    }
}
