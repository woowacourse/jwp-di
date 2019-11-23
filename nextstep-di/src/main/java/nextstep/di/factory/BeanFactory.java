package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.BeanInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanConstructor> constructors;

    public BeanFactory(Set<BeanConstructor> beanConstructors) {
        constructors = new HashMap<>();
        beanConstructors.stream()
                .forEach(ctor -> constructors.put(ctor.getReturnType(), ctor));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        constructors.keySet().forEach(this::getOrInstantiate);
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
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error("Error while instantiate bean", e);
            throw new BeanInitializationException(e);
        }
    }

    private Object instantiateBean(Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanConstructor ctor = constructors.get(clazz);
        Object[] params = resolveConstructorParameters(ctor);

        return ctor.construct(params);
    }

    private Object[] resolveConstructorParameters(BeanConstructor ctor) {
        return Arrays.stream(ctor.getParameterTypes())
                .map(param -> BeanFactoryUtils.findConcreteClass(param, constructors.keySet())
                        .orElse(param))
                .map(this::getOrInstantiate)
                .toArray();
    }

    public Set<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(cls -> cls.isAnnotationPresent(annotation))
                .map(beans::get)
                .collect(Collectors.toSet());
    }
}
