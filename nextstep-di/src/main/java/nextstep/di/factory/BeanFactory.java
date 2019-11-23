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
    private Map<Class<?>, BeanDefinition> beanDefinitions;

    public BeanFactory(Set<BeanDefinition> beanDefinitions) {
        this.beanDefinitions = new HashMap<>();
        beanDefinitions.stream()
                .forEach(def -> this.beanDefinitions.put(def.getReturnType(), def));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        beanDefinitions.keySet().forEach(this::getOrInstantiate);
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
        BeanDefinition beanDefinition = beanDefinitions.get(clazz);
        Object[] params = resolveInstantiateParameters(beanDefinition);

        return beanDefinition.instantiate(params);
    }

    private Object[] resolveInstantiateParameters(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(param -> BeanFactoryUtils.findConcreteClass(param, beanDefinitions.keySet()))
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
