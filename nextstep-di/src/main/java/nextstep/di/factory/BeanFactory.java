package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static nextstep.di.factory.BeanFactoryUtils.findConcreteClass;
import static nextstep.di.factory.BeanFactoryUtils.getInjectedConstructor;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static final String TAG = "BeanFactory";

    private static final int DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER = 0;

    private final Set<Class<?>> preInstanticateBeans;
    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.values().stream()
                .filter(value -> value.getClass().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Object::getClass, value -> value));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        this.preInstanticateBeans.forEach(clazz -> {
            Set<Class<?>> waitingForInitializationBeans = new HashSet<>();
            initBean(clazz, waitingForInitializationBeans);
        });
    }

    private void initBean(Class<?> clazz, Set<Class<?>> waitingForInitializationBeans) {
        Class<?> concreteClass = findConcreteClass(clazz, preInstanticateBeans);
        if (beans.containsKey(concreteClass)) {
            return;
        }

        validateInitialization(waitingForInitializationBeans, concreteClass);

        beans.put(concreteClass, createBean(concreteClass, waitingForInitializationBeans));
    }

    private void validateInitialization(Set<Class<?>> waitingForInitializationBeans, Class<?> concreteClass) {
        isValidBean(concreteClass);
        isCircularReference(concreteClass, waitingForInitializationBeans);
        waitingForInitializationBeans.add(concreteClass);
    }

    private void isValidBean(Class<?> concreteClass) {
        if (!preInstanticateBeans.contains(concreteClass)) {
            throw new IllegalStateException("해당 객체가 빈으로 등록되지 않았습니다.");
        }
    }

    private void isCircularReference(Class<?> clazz, Set<Class<?>> waitingForInitializationBeans) {
        if (waitingForInitializationBeans.contains(clazz)) {
            throw new IllegalStateException("순환 참조가 발생합니다.");
        }
    }

    private Object createBean(Class<?> concreteClass, Set<Class<?>> waitingForInitializationBeans) {
        logger.info("{}.createBean() >> {}", TAG, concreteClass);

        Constructor<?> constructor = getConstructor(concreteClass);
        List<Object> parameters = getParameterizedBeans(constructor, waitingForInitializationBeans);

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
        return Arrays.stream(concreteClass.getConstructors())
                .filter(this::isDefaultConstructor)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("기본 생성자가 존재하지 않습니다."));
    }

    private boolean isDefaultConstructor(Constructor<?> beanConstructor) {
        return beanConstructor.getParameterCount() == DEFAULT_CONSTRUCTOR_PARAMETER_NUMBER;
    }

    private List<Object> getParameterizedBeans(Constructor<?> constructor, Set<Class<?>> waitingForInitializationBeans) {
        Class<?>[] parameterTypes = Objects.requireNonNull(constructor).getParameterTypes();

        return Arrays.stream(parameterTypes)
                .map(type -> getParameterizedBean(type, waitingForInitializationBeans))
                .collect(Collectors.toList());
    }

    private Object getParameterizedBean(Class<?> type, Set<Class<?>> waitingForInitializationBeans) {
        if (Objects.isNull(getBeanInternal(type))) {
            initBean(type, waitingForInitializationBeans);
        }

        return getBeanInternal(type);
    }

    private Object getBeanInternal(Class<?> type) {
        return beans.get(findConcreteClass(type, preInstanticateBeans));
    }

    private Object createInstance(Constructor<?> constructor, List<Object> parameters) {
        try {
            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }


}
