package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        for (Class clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(Service.class)) {
                Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

                Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();

                Object[] constructors = getParameterInstances(parameterTypes);

                try {
                    beans.put(clazz, injectedConstructor.newInstance(constructors));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private Object[] getParameterInstances(Class<?>[] clazzs) {
        Object[] constructors = new Object[clazzs.length];
        for (int i = 0; i < clazzs.length; i++) {
            Class parameterType = clazzs[i];

            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);
            try {
                constructors[i] = concreteClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return constructors;
    }
}
