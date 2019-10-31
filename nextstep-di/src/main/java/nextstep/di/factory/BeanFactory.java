package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    public void initialize() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            beans.put(preInstanticateBean, createBean(preInstanticateBean));
        }
    }

    private Object createBean(Class<?> preInstanticateBean) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (beans.get(preInstanticateBean) != null) {
            return beans.get(preInstanticateBean);
        }

        Class<?> concrete = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans);

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(concrete);

        if (constructor == null) {
            Constructor<?> constructor1 = Arrays.stream(concrete.getDeclaredConstructors())
                    .filter(cons -> cons.getParameterCount() == 0)
                    .findAny()
                    .orElseThrow(IllegalAccessError::new);
            return constructor1.newInstance();
        }

        Class[] parameterTypes = constructor.getParameterTypes();
        List<Object> objects = new ArrayList<>();
        for (Class parameterType : parameterTypes) {
            Object o = beans.get(parameterType);
            if (o == null) {
                objects.add(createBean(parameterType));
            } else {
                objects.add(o);
            }
        }
        return constructor.newInstance(objects.toArray());
    }
}
