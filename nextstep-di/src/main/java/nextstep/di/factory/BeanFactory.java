package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.di.factory.exception.ScannerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
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
        preInstantiateBeans.forEach(this::instantiate);
    }

    private Object instantiate(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (constructor != null) {
            Object inst = instantiateConstructor(constructor);
            beans.put(clazz, inst);
            return inst;
        }

        try {
            Object inst = clazz.getDeclaredConstructor().newInstance();
            beans.put(clazz, inst);
            return inst;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new ScannerException(e);
        }
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : parameterTypes) {
            if (beans.containsKey(clazz)) {
                args.add(beans.get(clazz));
            } else {
                args.add(instantiate(BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans)));
            }
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

}
