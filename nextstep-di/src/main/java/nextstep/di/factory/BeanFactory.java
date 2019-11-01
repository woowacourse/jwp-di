package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanFactoryException;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiatedBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiatedBeans) {
        this.preInstantiatedBeans = preInstantiatedBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInstantiatedBeans.forEach(this::createBean);
    }

    private Object createBean(final Class<?> preInstantiatedBean) {
        if (beans.containsKey(preInstantiatedBean)) {
            return beans.get(preInstantiatedBean);
        }
        Object bean = createInstance(preInstantiatedBean);
        beans.put(preInstantiatedBean, bean);
        return bean;
    }

    private Object createInstance(final Class<?> preInstantiatedBean) {
        try {
            Constructor<?> constructor = getConstructor(preInstantiatedBean);
            List<Object> parameters = createParameters(constructor.getParameterTypes());

            return constructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanFactoryException(e);
        }
    }

    private Constructor<?> getConstructor(final Class<?> preInstantiatedBean) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(preInstantiatedBean, preInstantiatedBeans);
        Optional<Constructor<?>> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        return injectedConstructor
                .orElseGet(() -> {
                    try {
                        return concreteClass.getConstructor();
                    } catch (NoSuchMethodException e) {
                        logger.error(e.getMessage());
                        throw new BeanFactoryException(e);
                    }
                });
    }

    private List<Object> createParameters(final Class<?>[] parameterTypes) {
        return Stream.of(parameterTypes)
                .map(this::createBean)
                .collect(Collectors.toList());
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
