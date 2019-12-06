package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.exception.ParameterIsNotBeanException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentFactory {
    private final Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> components = Maps.newHashMap();

    public ComponentFactory(final BeanDefinition beanDefinition) {
        this.preInstantiateBeans = beanDefinition.getPreInstantiateComponents();
    }

    public Map<Class<?>, Object> initialize() {
        preInstantiateBeans
            .forEach(this::instantiateComponent);

        return components;
    }

    private Object instantiateComponent(final Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        if (components.containsKey(concreteClass)) {
            return components.get(concreteClass);
        }

        return BeanFactoryUtils.getInjectedConstructor(concreteClass)
            .map(this::instantiateConstructor)
            .orElseGet(() -> createBeanDefaultConstructor(concreteClass));
    }

    private Object instantiateConstructor(final Constructor<?> injectedConstructor) {
        Class<?> clazz = injectedConstructor.getDeclaringClass();
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();

        CircularChecker circularChecker = new CircularChecker(preInstantiateBeans);
        circularChecker.check(clazz);

        List<Object> parameterObject = instantiateParameter(parameterTypes);
        Object bean = BeanUtils.instantiateClass(injectedConstructor, parameterObject.toArray());

        components.put(clazz, bean);
        return bean;
    }

    private List<Object> instantiateParameter(final Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
            .map(parameterType -> {
                checkParameterIsBean(parameterType);
                Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
                return getParameter(concreteClass, parameterType);
            })
            .collect(Collectors.toList());
    }

    private void checkParameterIsBean(final Class<?> parameterType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
        if (preInstantiateBeans.contains(concreteClass)) {
            instantiateBean(concreteClass);
            return;
        }
        throw new ParameterIsNotBeanException();
    }

    private void instantiateBean(final Class<?> parameterType) {
        if (BeanFactoryUtils.getInjectedConstructor(parameterType).isEmpty()) {
            createBeanDefaultConstructor(parameterType);
        }
    }

    public Object getParameter(final Class<?> concreteClass, final Class<?> parameterType) {
        if (components.containsKey(concreteClass)) {
            return components.get(concreteClass);
        }
        return instantiateComponent(parameterType);
    }

    private Object createBeanDefaultConstructor(final Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object bean = BeanUtils.instantiateClass(constructor);
            components.put(clazz, bean);
            return bean;
        } catch (NoSuchMethodException e) {
            throw new DefaultConstructorFindFailException();
        }
    }
}
