package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.exception.BeanFactoryException;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public DefaultBeanFactory(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = new HashSet<>(preInstantiatedBeans);

        Map<Class<?>, Method> methodBeans = preInstantiatedBeans.stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toMap(Method::getReturnType, x -> x, (p1, p2) -> p2));

        methodBeans.values().stream()
                .map(method -> {
                    try {
                        logger.debug("method: {}, methodParams: {}", method, method.getParameterTypes());
                        Object[] params = Arrays.stream(method.getParameterTypes())
                                .map(x -> createMethodBean(x, methodBeans))
                                .toArray();

                        return method.invoke(getOrCreateBean(method.getDeclaringClass()), params);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException();
                    }
                })
                .forEach(y -> {
                    beans.put(y.getClass(), y);
                    this.preInstantiatedBeans.add(y.getClass());
                });

        preInstantiatedBeans.forEach(this::createBean);
    }

    private Object createMethodBean(final Class<?> clazz, final Map<Class<?>, Method> methodBeans) {
        final Object bean = beans.get(clazz);
        if (bean == null) {
            final Method method = methodBeans.get(clazz);
            try {
                return method.invoke(getOrCreateBean(method.getDeclaringClass()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return bean;
    }

    private Object getOrCreateBean(Class<?> requiredType) {
        Object bean = getBean(requiredType);
        return bean != null ? bean : createBean(requiredType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(requiredType, preInstantiatedBeans);
        return (T) beans.get(concreteClass);
    }

    private Object createBean(final Class<?> preInstantiatedBean) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(preInstantiatedBean, preInstantiatedBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }
        Object bean = createInstance(concreteClass);
        beans.put(concreteClass, bean);
        preInstantiatedBeans.add(concreteClass);
        return bean;
    }

    private Object createInstance(final Class<?> preInstantiatedBean) {
        try {
            Constructor<?> constructor = getBeanConstructor(preInstantiatedBean);
            List<Object> parameters = createParameters(constructor.getParameterTypes());

            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanFactoryException(e);
        }
    }

    private Constructor<?> getBeanConstructor(final Class<?> preInstantiatedBean) {
        Optional<Constructor<?>> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstantiatedBean);
        return injectedConstructor.orElseGet(() -> getConstructor(preInstantiatedBean));
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            throw new BeanFactoryException(e);
        }
    }

    private List<Object> createParameters(final Class<?>[] parameterTypes) {
        return Stream.of(parameterTypes)
                .map(this::createBean)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(classAnnotation))
                .map(clazz -> ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(methodAnnotation)))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
