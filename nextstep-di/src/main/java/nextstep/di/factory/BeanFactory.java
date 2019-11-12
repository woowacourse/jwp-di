package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
        initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        try {
            preInstantiateBeans.forEach(this::getInstantiateClass);
        } catch (ScannerException | NotRegisteredBeanException e) {
            throw new InstantiateBeansException(e.getMessage(), e.getCause());
        }
    }

    private Object registerInstantiatedBean(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);

        if (beans.containsKey(concreteClass)) {
            getBean(concreteClass);
        }

        Object instance = instantiateClass(concreteClass);
        beans.put(concreteClass, instance);
        return instance;
    }

    private Object instantiateClass(Class<?> clazz) {
        checkPreInstantiateBean(clazz);
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        return Optional.ofNullable(constructor)
                .map(this::instantiateConstructor)
                .orElseGet(() -> defaultConstructorInstantiate(clazz));
    }

    private void checkPreInstantiateBean(Class<?> clazz) {
        if (!preInstantiateBeans.contains(clazz)) {
            throw new NotRegisteredBeanException();
        }
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
            throw new InstantiateBeansException(e);
        }
    }

    public Map<Class<?>, Object> getControllers() {
        Map<Class<?>, Object> controllers = Maps.newHashMap();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                controllers.put(clazz, getBean(clazz));
            }
        }
        return controllers;
    }
}
