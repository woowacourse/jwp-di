package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Object... basePackage) {
        preInstantiateBeans = new BeanScanner(basePackage).getAnnotatedTypes();
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

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        Set<Class<?>> types = Sets.newHashSet();
        for (Class<?> clazz : preInstantiateBeans) {
            if (clazz.isAnnotationPresent(annotation)) {
                types.add(clazz);
            }
        }
        return types;
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
