package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanNotFoundException;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.exception.ParameterIsNotBeanException;
import nextstep.stereotype.Controller;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if(beans.containsKey(requiredType)) {
            return (T)beans.get(requiredType);
        }
        throw new BeanNotFoundException();
    }

    public void initialize() {
        preInstantiateBeans.forEach(this::instantiateClass);
    }

    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

        return BeanFactoryUtils.getInjectedConstructor(concreteClass)
            .map(this::instantiateConstructor)
            .orElseGet(() -> createBeanDefaultConstructor(concreteClass));
    }

    private Object instantiateConstructor(Constructor<?> ownerConstructor) {
        Class<?>[] parameterTypes = ownerConstructor.getParameterTypes();
        List<Object> parameterObject = instantiateParameter(ownerConstructor, parameterTypes);
        Class<?> clazz = ownerConstructor.getDeclaringClass();
        Object bean = BeanUtils.instantiateClass(ownerConstructor, parameterObject.toArray());
        beans.put(clazz, bean);
        return bean;
    }

    private List<Object> instantiateParameter(Constructor<?> ownerConstructor, Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
            .map(parameterType -> {
                checkParameterIsBean(ownerConstructor, parameterType);
                Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
                return getParameter(concreteClass, parameterType);
            })
            .collect(Collectors.toList());
    }

    private void checkParameterIsBean(Constructor<?> ownerConstructor, Class<?> parameterType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
        if(preInstantiateBeans.contains(concreteClass)) {
            instantiateBean(ownerConstructor,concreteClass);
            return;
        }
        throw new ParameterIsNotBeanException();
    }

    private void instantiateBean(Constructor<?> ownerConstructor, Class<?> parameterType) {
        BeanFactoryUtils.getInjectedConstructor(parameterType)
            .ifPresentOrElse((injectConstructor) -> checkCircularReference(ownerConstructor, injectConstructor),
                () ->createBeanDefaultConstructor(parameterType));
    }

    private void checkCircularReference(Constructor<?> ownerConstructor, Constructor<?> injectConstructor) {
        Parameter[] parameters = injectConstructor.getParameters();
        for (Parameter parameter : parameters) {
            checkSameClass(ownerConstructor, parameter);
        }
    }

    private void checkSameClass(Constructor<?> ownerConstructor, Parameter parameter) {
        if(parameter.getType().getName() == ownerConstructor.getName()){
            throw new CircularReferenceException();
        }
    }

    public Object getParameter(Class<?> concreteClass, Class<?> parameterType) {
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }
        return instantiateClass(parameterType);
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
        return beans.keySet()
            .stream()
            .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
            .collect(Collectors.toMap(clazz -> clazz, clazz -> beans.get(clazz)));
    }
}
