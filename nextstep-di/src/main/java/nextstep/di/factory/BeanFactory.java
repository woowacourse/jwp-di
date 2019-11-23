package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final int NONE = 0;

    private final Map<Class<?>, BeanDefinition> beanDefinitions;

    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Map<Class<?>, BeanDefinition> beanDefinitions) {
        this.beanDefinitions = beanDefinitions;
    }

    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            getOrInstantiateBean(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Class<?>> types = Sets.newHashSet();
        for (Class<?> clazz : beanDefinitions.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                types.add(clazz);
            }
        }
        return types;
    }

    private Object getOrInstantiateBean(Class<?> clazz) {
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
        if (beanDefinition.getParameterTypes().length == NONE) {
            return beanDefinition.instantiate();
        }
        return beanDefinition.instantiate(getParameters(beanDefinition));
    }

    private Object[] getParameters(BeanDefinition beanDefinition) {
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        List<Object> parameters = Lists.newArrayList();
        for (Class<?> clazz : parameterTypes) {
            parameters.add(getOrInstantiateBean(clazz));
        }
        return parameters.toArray();
    }
}
