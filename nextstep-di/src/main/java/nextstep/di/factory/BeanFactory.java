package nextstep.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeanUtils;

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
        preInstanticateBeans.forEach(this::instantiateClass);
    }

    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

        return BeanFactoryUtils.getInjectedConstructor(concreteClass)
            .map(this::instantiateConstructor)
            .orElseGet(() -> createBeanDefaultConstructor(concreteClass));
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameterObject = Lists.newArrayList();

        for (Class<?> parameterType : parameterTypes) {
            instantiateParameter(parameterObject, parameterType);
        }

        Class<?> clazz = constructor.getDeclaringClass();
        Object bean = BeanUtils.instantiateClass(constructor, parameterObject.toArray());
        beans.put(clazz, bean);
        return bean;
    }

    private void instantiateParameter(List<Object> parameterObject, Class<?> aClass) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(aClass, preInstanticateBeans);
        if (beans.containsKey(concreteClass)) {
            parameterObject.add(beans.get(concreteClass));
            return;
        }
        parameterObject.add(instantiateClass(aClass));
    }

    private Object createBeanDefaultConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object bean = BeanUtils.instantiateClass(constructor);
            beans.put(clazz, bean);
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
