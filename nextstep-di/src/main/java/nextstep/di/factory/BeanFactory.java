package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.CannotCreateInstance;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory() {

    }

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        if (Objects.nonNull(preInstantiateBeans)) {
            for (Class<?> clazz : preInstantiateBeans) {
                instantiate(clazz);
            }
        }
    }

    public Object instantiate(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        return createSingleInstance(clazz);
    }

    public void addBean(Class<?> clazz, Object instance) {
        beans.put(clazz, instance);
    }

    private Object createSingleInstance(Class<?> clazz) {
        Object instance = createInstance(clazz);
        addBean(clazz, instance);

        return instance;
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = getConstructor(clazz);

            return constructor.newInstance(getParameters(constructor));
        } catch (Exception e) {
            logger.error("createInstance >> ", e);
            throw new CannotCreateInstance(e);
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (Objects.isNull(constructor)) {
            return BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans).getConstructor();
        }

        return constructor;
    }

    private Object[] getParameters(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(clazz -> BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans))
                .map(this::instantiate)
                .toArray();
    }

    public Set<Class<?>> getController() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }
}
