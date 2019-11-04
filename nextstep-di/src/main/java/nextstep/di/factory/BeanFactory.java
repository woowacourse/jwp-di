package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static nextstep.di.factory.BeanFactoryUtils.findConcreteClass;
import static nextstep.di.factory.BeanFactoryUtils.getInjectedConstructor;

// TODO 순환참조 좀..
public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static final String TAG = "BeanFactory";

    private static final int DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER = 0;

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        Map<Class<?>, Object> newBeans = Maps.newHashMap();
        for (Object value : beans.values()) {
            boolean annotationPresent = value.getClass().isAnnotationPresent(annotation);
            if (annotationPresent) {
                newBeans.put(value.getClass(), value);
            }

        }
        return newBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        this.preInstanticateBeans.forEach(clazz -> beans.put(clazz, initBean(clazz)));
    }

    private Object initBean(Class<?> clazz) {
        Class<?> concreteClass = findConcreteClass(clazz, preInstanticateBeans);
        logger.info("{}.initBean() >> {}", TAG, concreteClass);

        Constructor<?> constructor = getConstructor(concreteClass);
        List<Object> parameters = getParameterBeans(constructor);

        return createInstance(constructor, parameters);
    }

    private Constructor<?> getConstructor(Class<?> concreteClass) {
        Constructor<?> constructor = getInjectedConstructor(concreteClass);

        if (Objects.isNull(constructor)) {
            constructor = getDefaultConstructor(concreteClass);
        }

        return constructor;
    }

    private Constructor<?> getDefaultConstructor(Class<?> concreteClass) {
        //TODO 커스텀 에러 만들기
        return Arrays.stream(concreteClass.getConstructors())
                .filter(this::isDefaultConstructor)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isDefaultConstructor(Constructor<?> beanConstructor) {
        return beanConstructor.getParameterCount() == DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER;
    }

    private List<Object> getParameterBeans(Constructor<?> constructor) {
        List<Object> parameters = new ArrayList<>();
        Class<?>[] types = Objects.requireNonNull(constructor).getParameterTypes();

        for (Class<?> type : types) {
            parameters.add(getParameterBean(type));
        }

        return parameters;
    }

    private Object getParameterBean(Class<?> type) {
        return beans.containsKey(type) ? beans.get(type) : initBean(type);
    }

    private Object createInstance(Constructor<?> constructor, List<Object> parameters) {
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
