package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.di.factory.exception.ScannerException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Set<Method> preInvokedBeanMethods;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory() {
    }

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
        classPathInitializer();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInvokedBeanMethods.forEach(method -> {
            try {
                Object object = method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance());
                System.out.println(object.getClass());
                beans.put(object.getClass(), object);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                throw new ScannerException(e);
            }
        });
    }

    public void classPathInitializer() {
        preInstantiateBeans.forEach(this::getInstantiateClass);
    }

    public void configurationInitializer(Set<Method> preInvokedBeanMethods) {
        this.preInvokedBeanMethods = preInvokedBeanMethods;
    }

    private Object getInstantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);

        if (beans.containsKey(concreteClass)) {
            getBean(concreteClass);
        }

        Object instance = instantiateClass(concreteClass);
        beans.put(concreteClass, instance);
        return instance;
    }

    private Object instantiateClass(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        return Optional.ofNullable(constructor)
                .map(this::instantiateConstructor)
                .orElseGet(() -> defaultConstructorInstantiate(clazz));
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();

        for (Class<?> clazz : parameterTypes) {
            args.add(getInstantiateClass(clazz));
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    public Object defaultConstructorInstantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error("Fail to instantiate by default constructor : ", e);
            throw new ScannerException(e);
        }
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

    public void addPreInstantiateBeans(Class<?> clazz) {
        preInstantiateBeans.add(clazz);
    }
}
