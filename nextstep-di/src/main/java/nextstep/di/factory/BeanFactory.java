package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
        initialize();
    }

    private void initialize() {
        preInstantiateBeans.forEach(clazz -> {
            try {
                instantiate(clazz);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException("빈 초기화 실패");
            }
        });
    }

    private Object instantiate(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        Object bean = Objects.isNull(constructor) ? BeanUtils.instantiateClass(clazz) : constructor.newInstance(getParameters(constructor));
        beans.put(clazz, bean);

        return beans.get(clazz);
    }

    private Object[] getParameters(Constructor constructor) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        List<Object> arguments = new ArrayList<>();
        Class[] parameterTypes = constructor.getParameterTypes();

        for (Class clazz : parameterTypes) {
            Class cls = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            arguments.add(instantiate(cls));
        }

        return arguments.toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, beans.get(clazz));
            }
        }
        return controllers;
    }
}
