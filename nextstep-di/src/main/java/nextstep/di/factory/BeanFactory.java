package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Set<Class<?>> preInstantiateBeans;
    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Object... basePackage) {
        this.preInstantiateBeans = (new BeanScanner(basePackage)).getPreInstantiateBeans();
    }

    public BeanFactory initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            beans.put(preInstantiateBean, instantiateClass(preInstantiateBean));
        }
        return this;
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
            Object bean = beans.get(clazz);
            if (bean != null) {
                args.add(bean);
            } else {
                args.add(instantiateClass(clazz));
            }
        }
        return BeanUtils.instantiateClass(constructor, args.toArray());
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) this.beans.get(requiredType);
    }

    public Map<Class<?>, Object> getAllWithAnnotation(Class<? extends Annotation> annotation) {
        return this.beans.entrySet().stream().filter(x -> x.getKey().isAnnotationPresent(annotation))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}