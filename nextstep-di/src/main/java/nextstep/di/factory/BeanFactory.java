package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.exception.DefaultConstructorFindFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInstanticateBeans.forEach(this::instantiateClass);
    }

    private Object instantiateClass(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

    private void createInstance(Constructor<?> constructor) {
        try {
            addBean(constructor);
        } catch (Exception e) {
            throw new BeanCreateFailException();
        }
    }

    private Constructor<?>[] getBeanConstructors(Class<?> clazz) {
        Class<?> result = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
        logger.debug(">>>{}", result.getName());
        return result.getConstructors();
    }

    private void instantiateConstructor(Constructor<?> constructor) {
        Annotation[] annotations = constructor.getDeclaredAnnotations();

        if (annotations.length == 0) {
            createInstance(constructor);
            return;
        }

        for (Annotation annotation : annotations) {
            instantiateInAnnotation(constructor, annotation);
        }
    }

    private void instantiateInAnnotation(Constructor<?> constructor, Annotation annotation) {
        if (annotation.annotationType().equals(Inject.class)) {
            instantiateParameter(constructor);
        }
        createInstance(constructor);
    }

    private void instantiateParameter(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters) {
            createBean(parameter.getType());
        }
    }

    private void addBean(Constructor<?> constructor) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        Class<?> clazz = constructor.getDeclaringClass();
        if (!beans.containsKey(clazz)) {
            beans.put(clazz, constructor.newInstance(getParameterInstance(constructor).toArray()));
        }
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
}
