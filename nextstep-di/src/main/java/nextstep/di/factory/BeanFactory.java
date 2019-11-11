package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanInstantiationException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        for (Class<?> clazz : preInstantiateBeans) {
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
            return createInstance(clazz, BeanFactoryUtils.getInjectedConstructor(clazz));
        } catch (Exception e) {
            logger.debug("Fail to instantiate bean {}", e.getMessage());
            throw new BeanInstantiationException();
        }
    }

    private Object createInstance(Class<?> clazz, Constructor<?> constructor) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (Objects.isNull(constructor)) {
            return clazz.newInstance();
        }
        return constructor.newInstance(getParameters(constructor));
    }

    private Object[] getParameters(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(clazz -> BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans))
                .map(this::putBean)
                .toArray();
    }

    public Set<Class<?>> getController() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }
}
