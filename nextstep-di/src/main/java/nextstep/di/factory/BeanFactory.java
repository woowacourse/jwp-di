package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.exception.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Object... basePackage) {
        preInstantiateBeans = new ClasspathBeanScanner(basePackage).getAnnotatedTypes();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            getOrInstantiateBean(preInstantiateBean);
        }
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Class<?>> types = Sets.newHashSet();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(annotation)) {
                types.add(clazz);
            }
        }
        return types;
    }

    private Object getOrInstantiateBean(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }
        Object instance = instantiateClass(concreteClass);
        beans.put(concreteClass, instance);
        return instance;
    }

    private Object instantiateClass(Class<?> clazz) {
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (constructor != null) {
            return instantiateConstructor(constructor);
        }
        return createInstance(findDefaultConstructor(clazz));
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : parameterTypes) {
            args.add(getOrInstantiateBean(clazz));
        }
        return createInstance(constructor, args.toArray());
    }

    private Constructor<?> findDefaultConstructor(Class<?> clazz) {
        try {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
            return concreteClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }

    private Object createInstance(Constructor<?> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }
}
