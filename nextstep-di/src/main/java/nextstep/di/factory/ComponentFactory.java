package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.exception.ParameterIsNotBeanException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentFactory {
    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> components = Maps.newHashMap();

    public ComponentFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public Object instantiateComponent(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        if (components.containsKey(concreteClass)) {
            return components.get(concreteClass);
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
        components.put(clazz, bean);
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
        if (preInstantiateBeans.contains(concreteClass)) {
            instantiateBean(ownerConstructor, concreteClass);
            return;
        }
        throw new ParameterIsNotBeanException();
    }

    private void instantiateBean(Constructor<?> ownerConstructor, Class<?> parameterType) {
        BeanFactoryUtils.getInjectedConstructor(parameterType)
            .ifPresentOrElse((injectConstructor) -> checkCircularReference(ownerConstructor, injectConstructor),
                () -> createBeanDefaultConstructor(parameterType));
    }

    private void checkCircularReference(Constructor<?> ownerConstructor, Constructor<?> againstOwnerConstructor) {
        Parameter[] parameters = againstOwnerConstructor.getParameters();
        for (Parameter parameter : parameters) {
            checkSameClass(ownerConstructor, parameter);
        }
    }

    private void checkSameClass(Constructor<?> ownerConstructor, Parameter parameter) {
        if (parameter.getType().getName() == ownerConstructor.getName()) {
            throw new CircularReferenceException();
        }
    }

    public Object getParameter(Class<?> concreteClass, Class<?> parameterType) {
        if (components.containsKey(concreteClass)) {
            return components.get(concreteClass);
        }
        return instantiateComponent(parameterType);
    }

    private Object createBeanDefaultConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object bean = BeanUtils.instantiateClass(constructor);
            components.put(clazz, bean);
            return bean;
        } catch (NoSuchMethodException e) {
            throw new DefaultConstructorFindFailException();
        }
    }

    public Map<Class<?>, Object> getComponents() {
        return Collections.unmodifiableMap(components);
    }
}
