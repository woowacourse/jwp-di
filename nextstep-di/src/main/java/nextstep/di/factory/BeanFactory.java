package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitions;

    public void init(final Map< Class<?>, BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }

    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            instantiate(clazz);
        }
    }

    private Object instantiate(Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanDefinitions.keySet());

        if (beans.containsKey(concreteClass)) {
            return beans.get(concreteClass);
        }

        Object instance = instantiateClass(concreteClass);
        beans.put(concreteClass, instance);

        return instance;
    }

    private Object instantiateClass(Class<?> concreteClass) {
        BeanDefinition beanDefinition = beanDefinitions.get(concreteClass);

        if (beanDefinition.getParameterTypes().length == 0) {
            return beanDefinition.instantiate();
        }

        return beanDefinition.instantiate(getParameters(beanDefinition));
    }

    private Object[] getParameters(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(this::instantiate)
                .toArray();
    }

    public Set<Class<?>> getController() {
        return beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }
}
