package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
        for (Class<?> clazz : preInstantiateBeans) {
            beans.put(clazz, instantiate(clazz));
        }
    }

    private Object instantiate(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (Objects.isNull(constructor)) {
            return BeanUtils.instantiateClass(clazz);
        }

        return BeanUtils.instantiateClass(constructor, getParameters(constructor));
    }

    private Object[] getParameters(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(clazz -> BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans))
                .map(this::instantiate)
                .toArray();
    }
}
