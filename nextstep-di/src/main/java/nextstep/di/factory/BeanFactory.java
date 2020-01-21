package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.exception.BeanCreateFailException;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.DefaultConstructorFindFailException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedBeans = Sets.newHashSet();

    private Map<Class<?>, Method> methodsOfBeans = Maps.newHashMap();

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        methodsOfBeans.keySet().forEach(this::instantiateClassWithMethodsOfBeans);
        preInstantiatedBeans.forEach(this::instantiateClass);
        log.info("Initialized BeanFactory!");
    }

    public Object instantiateClassWithMethodsOfBeans(Class<?> clazz) {
        Method method = methodsOfBeans.get(clazz);
        Object bean = BeanFactoryUtils.instantiateClass(method, getParametersOfExecutable(method));
        beans.put(clazz, bean);
        return bean;
    }

    private Object instantiateClass(Class<?> clazz) {
        if (containsMethodOfBeans(clazz)) {
            return instantiateClassWithMethodsOfBeans(clazz);
        }

        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiatedBeans);
        if (isNotBean(concreteClass)) {
            log.debug("Not Bean : {}", concreteClass.getName());
            throw new BeanCreateFailException();
        }

        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        if (Objects.nonNull(constructor)) {
            return instantiateConstructorWithInject(constructor);
        }

        log.info("Instantiate class! : {}", clazz.getName());
        return instantiateDefaultConstructor(concreteClass);
    }

    private boolean containsMethodOfBeans(Class<?> clazz) {
        return methodsOfBeans.containsKey(clazz);
    }

    private Object[] getParametersOfExecutable(Executable executable) {
        Class<?>[] parameterTypes = executable.getParameterTypes();
        List<Object> parameterObject = Lists.newArrayList();

        for (Class<?> parameterType : parameterTypes) {
            if (containsMethodOfBeans(parameterType)) {
                parameterObject.add(instantiateClass(parameterType));
                continue;
            }
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiatedBeans);
            confirmCircularReference(executable, concreteClass);
            parameterObject.add(getParameterOfConstructor(parameterType, concreteClass));
        }

        log.info("Get parameters : {}", executable.getName());
        return parameterObject.toArray();
    }

    private boolean isNotBean(Class<?> clazz) {
        return !preInstantiatedBeans.contains(clazz);
    }

    private Object instantiateConstructorWithInject(Constructor<?> constructor) {
        Class<?> clazz = constructor.getDeclaringClass();
        Object bean = BeanUtils.instantiateClass(constructor, getParametersOfExecutable(constructor));
        log.debug("Instantiate Bean with Inject Annotation : {}", clazz.getSimpleName());
        beans.put(clazz, bean);
        return bean;
    }

    private void confirmCircularReference(Executable executable, Class<?> concreteClass) {
        if (isSameClassType(executable, concreteClass)) {
            log.debug("Circular Reference : {}, {}", executable.getName(), concreteClass.getName());
            throw new CircularReferenceException();
        }
    }

    private boolean isSameClassType(Executable executable, Class<?> concreteClass) {
        return executable.getDeclaringClass().equals(concreteClass);
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
            log.debug("Instantiate Bean with Default Constructor: {}", clazz.getSimpleName());
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

    public void appendPreInstantiatedBeans(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans.addAll(preInstantiatedBeans);
    }

    public void appendMethodsOfPreInstantiatedBeans(List<Method> methods) {
        methods.forEach(method -> methodsOfBeans.put(method.getReturnType(), method));
    }
}
