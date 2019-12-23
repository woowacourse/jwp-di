package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.CannotCreateInstance;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Method> preInstantiateMethodBeans;
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

    public void initClazz(final Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public void initConfigMethod(final Map<Class<?>, Method> preInstantiateMethodBeans) {
        this.preInstantiateMethodBeans = preInstantiateMethodBeans;
    }

    public void initialize() {
        if (Objects.nonNull(preInstantiateBeans)) {
            for (Class<?> clazz : preInstantiateBeans) {
                instantiate(clazz);
            }
        }

        if (Objects.nonNull(preInstantiateMethodBeans)) {
            for (Map.Entry<Class<?>, Method> beanMethod : preInstantiateMethodBeans.entrySet()) {
                instantiate(beanMethod.getKey(), beanMethod.getValue());
            }
        }
    }

    private Object instantiate(final Class<?> clazz, final Method method) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        return registerBean(method);
    }

    private Object registerBean(final Method method) {
        try {
            Object instance = method.getDeclaringClass().getConstructor().newInstance();
            Object object = method.invoke(instance, getParameters(method.getParameterTypes()));
            addBean(method.getReturnType(), object);

            return object;
        } catch (Exception e) {
            logger.error(">> registerBean", e);
            throw new CannotCreateInstance(e);
        }
    }


    private Object[] getParameters(final Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .map(clazz -> instantiate(clazz, preInstantiateMethodBeans.get(clazz)))
                .toArray();
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
        Object[] objects = Arrays.stream(constructor.getParameterTypes())
                .filter(clazz -> beans.containsKey(clazz))
                .map(clazz -> beans.get(clazz))
                .toArray();

        Object[] objects1 = Arrays.stream(constructor.getParameterTypes())
                .filter(clazz -> !beans.containsKey(clazz))
                .map(clazz -> BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans))
                .map(this::instantiate)
                .toArray();

        Object[] newObjects = new Object[objects.length + objects1.length];

        System.arraycopy(objects, 0 , newObjects, 0, objects.length);
        System.arraycopy(objects1, 0 , newObjects, objects.length, objects1.length);

        return newObjects;
    }

    public Set<Class<?>> getController() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }
}
