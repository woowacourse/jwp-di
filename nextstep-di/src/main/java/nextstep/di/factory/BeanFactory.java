package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Class<?>> getControllers() {
        return beans.keySet().stream()
                .filter(bean -> bean.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            Class concreteClass = findConcreteClass(preInstantiateBean);
            beans.put(concreteClass, createBean(concreteClass));
        }
    }

    private Object createBean(Class clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        return createInjectedInstance(clazz);
    }

    private Object createInjectedInstance(Class concreteClass) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

        if (injectedConstructor == null) {
            return createInstance(getDefaultConstructor(concreteClass));
        }

        List<Object> parameters = prepareParameterBeans(injectedConstructor);
        return createInstance(injectedConstructor, parameters.toArray());
    }

    private Object createInstance(Constructor constructor, Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Constructor getDefaultConstructor(Class concreteClass) {
        try {
            return concreteClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

    private List<Object> prepareParameterBeans(Constructor<?> injectedConstructor) {
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : injectedConstructor.getParameterTypes()) {
            Class parameter = findConcreteClass(parameterType);
            Object bean = createBean(parameter);
            parameters.add(bean);
        }

        return parameters;
    }

    private Class findConcreteClass(Class<?> clazz) {
        return BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
    }
}
