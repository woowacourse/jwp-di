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
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        preInstantiatedBeans.forEach(this::createBean);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object createBean(final Class<?> preInstantiatedBean) {
        if (beans.containsKey(preInstantiatedBean)) {
            return beans.get(preInstantiatedBean);
        }

        Optional<Constructor<?>> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstantiatedBean);

        Object bean = injectedConstructor.map(this::createInjectedBean)
                .orElseGet(() -> createConcreteClassBean(preInstantiatedBean));

        beans.put(preInstantiatedBean, bean);

        return bean;
    }

    private Object createInjectedBean(final Constructor<?> injectedConstructor) {
        try {
            Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
            List<Object> parameters = createParameters(parameterTypes);

            return injectedConstructor.newInstance(parameters.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanFactoryException(e);
        }
    }

    private List<Object> createParameters(final Class<?>[] parameterTypes) {
        return Stream.of(parameterTypes)
                .map(this::createBean)
                .collect(Collectors.toList());
    }

    private Object createConcreteClassBean(final Class<?> preInstantiatedBean) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(preInstantiatedBean, preInstantiatedBeans);
        return BeanFactoryUtils.getInjectedConstructor(concreteClass)
                .map(this::createInjectedBean)
                .orElseGet(() -> {
                    try {
                        return concreteClass.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        logger.error(e.getMessage());
                        throw new BeanFactoryException(e);
                    }
                });
    }
}
