package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.definition.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static nextstep.di.factory.BeanFactoryUtils.findConcreteClass;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);
    private static final String TAG = "BeanFactory";

    private final Map<Class<?>, BeanDefinition> preInstanticateBeans;
    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Map<Class<?>, BeanDefinition> preInstanticateBeans) {
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
        this.preInstanticateBeans.keySet().forEach(clazz -> {
            Set<Class<?>> waitingForInitializationBeans = new HashSet<>();
            initBean(clazz, waitingForInitializationBeans);
        });
    }

    private void initBean(Class<?> clazz, Set<Class<?>> waitingForInitializationBeans) {
        Class<?> clazz2 = getConcreteClassOr(clazz);

        if (isAlreadyInitializedBean(clazz2)) {
            return;
        }

        validateInitialization(waitingForInitializationBeans, clazz2);

        beans.put(clazz2, createBean(preInstanticateBeans.get(clazz2), waitingForInitializationBeans));
    }

    private boolean isAlreadyInitializedBean(Class<?> concreteClass) {
        return beans.containsKey(concreteClass);
    }

    private void validateInitialization(Set<Class<?>> waitingForInitializationBeans, Class<?> concreteClass) {
        validateBean(concreteClass);
        validateCircularReference(concreteClass, waitingForInitializationBeans);
        waitingForInitializationBeans.add(concreteClass);
    }

    private void validateBean(Class<?> concreteClass) {
        if (!preInstanticateBeans.containsKey(concreteClass)) {
            throw new IllegalStateException("해당 객체가 빈으로 등록되지 않았습니다.");
        }
    }

    private void validateCircularReference(Class<?> clazz, Set<Class<?>> waitingForInitializationBeans) {
        if (waitingForInitializationBeans.contains(clazz)) {
            throw new IllegalStateException("순환 참조가 발생합니다.");
        }
    }

    private Object createBean(BeanDefinition beanDefinition, Set<Class<?>> waitingForInitializationBeans) {
        logger.info("{}.createBean() >> {}", TAG, beanDefinition);

        List<Object> parameterizedBeans = getParameterizedBeans(beanDefinition, waitingForInitializationBeans);
        return beanDefinition.createBean(parameterizedBeans.toArray());
    }

    private List<Object> getParameterizedBeans(BeanDefinition beanDefinition, Set<Class<?>> waitingForInitializationBeans) {
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();

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
        return beans.get(getConcreteClassOr(type));
    }

    //TODO 뭔가,,,
    private Class<?> getConcreteClassOr(Class<?> type) {
        if (preInstanticateBeans.containsKey(type)) {
            return type;
        }

        return findConcreteClass(type, preInstanticateBeans.keySet());
    }
}
