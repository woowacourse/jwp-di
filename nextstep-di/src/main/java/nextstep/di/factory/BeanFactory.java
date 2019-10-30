package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.BeanFactoryInitializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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

    public void initialize() {
        preInstantiateBeans
                .forEach(this::getOrInstantiate);
    }

    private Object getOrInstantiate(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Object instance = tryInstantiateBean(clazz);
        beans.put(clazz, instance);
        return instance;
    }

    private Object tryInstantiateBean(Class<?> clazz) {
        try {
            return instantiateBean(clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new BeanFactoryInitializeException(e);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<?> ctor = getConstructor(clazz);
        Object[] params = resolveConstructorParameters(ctor);

        return ctor.newInstance(params);
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?> ctor = BeanFactoryUtils.getInjectedConstructor(clazz);
        return ctor == null ? clazz.getConstructor() : ctor;
    }

    private Object[] resolveConstructorParameters(Constructor<?> ctor) {
        return Arrays.stream(ctor.getParameterTypes())
                    .map(param -> BeanFactoryUtils.findConcreteClass(param, preInstantiateBeans))
                    .map(this::getOrInstantiate)
                    .toArray();
    }
}
