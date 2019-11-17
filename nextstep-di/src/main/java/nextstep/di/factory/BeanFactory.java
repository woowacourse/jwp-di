package nextstep.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = preInstantiatedBeans;
        preInstantiatedBeans.forEach(this::instantiateClass);
        logger.info("Initialized BeanFactory!");
    }

    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiatedBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        if (Objects.nonNull(constructor)) {
            return instantiateConstructorWithInject(constructor);
        }
        return instantiateDefaultConstructor(concreteClass);
    }

    private Object instantiateConstructorWithInject(Constructor<?> constructor) {
        Class<?> clazz = constructor.getDeclaringClass();
        Object bean = BeanUtils.instantiateClass(constructor, getParametersOfConstructor(constructor));
        logger.debug("Instantiate Bean with Inject Annotation : {}", clazz.getSimpleName());
        beans.put(clazz, bean);
        return bean;
    }

    private Object[] getParametersOfConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameterObject = Lists.newArrayList();

        for (Class<?> parameterType : parameterTypes) {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiatedBeans);
            confirmCircularReference(constructor, concreteClass);
            parameterObject.add(getParameterOfConstructor(parameterType, concreteClass));
        }
        return parameterObject.toArray();
    }

    private void confirmCircularReference(Constructor<?> constructor, Class<?> concreteClass) {
        if (isSameClassType(constructor, concreteClass)) {
            throw new CircularReferenceException();
        }
    }

    private boolean isSameClassType(Constructor<?> constructor, Class<?> concreteClass) {
        return constructor.getDeclaringClass().equals(concreteClass);
    }

    private Object getParameterOfConstructor(Class<?> parameterType, Class<?> concreteClass) {
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }
        return instantiateClass(parameterType);
    }

    private Object instantiateDefaultConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object bean = BeanUtils.instantiateClass(constructor);
            beans.put(clazz, bean);
            logger.debug("Instantiate Bean with Default Constructor: {}", clazz.getSimpleName());
            return bean;
        } catch (NoSuchMethodException e) {
            throw new DefaultConstructorFindFailException();
        }
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
