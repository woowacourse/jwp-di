package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
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
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            beans.put(preInstantiateBean, instantiateClass(preInstantiateBean));
        }
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        Map<Class<?>, Object> annotatedBeans = Maps.newHashMap();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(annotation)) {
                annotatedBeans.put(clazz, beans.get(clazz));
            }
        }
        return annotatedBeans;
    }

    private Object instantiateClass(Class<?> clazz) {
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (constructor != null) {
            return instantiateConstructor(constructor);
        }
        return BeanUtils.instantiateClass(BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans));
    }

    private Object instantiateConstructor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = Lists.newArrayList();
        for (Class<?> clazz : parameterTypes) {
            args.add(getOrInstantiateBean(clazz));
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    private Object getOrInstantiateBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        Object instance = instantiateClass(clazz);
        beans.put(clazz, instance);
        return instance;
    }
}
