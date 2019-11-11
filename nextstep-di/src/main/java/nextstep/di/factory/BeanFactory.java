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

import static java.util.Objects.isNull;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getBeansOf(final Class<? extends Annotation> annotation) {
        return beans.entrySet().stream()
            .filter(entry -> isClassAnnotationWith(entry.getKey(), annotation))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isClassAnnotationWith(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }

    public void initialize() {
        preInstanticateBeans.forEach(this::instantiateBean);
    }

    private Object instantiateBean(final Class<?> clazz) {
        if (isBeanExists(clazz)) {
            return beans.get(clazz);
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (hasNotInjected(injectedConstructor)) {
            return registerBean(clazz);
        }

        return registerBean(clazz, injectedConstructor);
    }

    private boolean isBeanExists(final Class<?> clazz) {
        return beans.containsKey(clazz);
    }

    private boolean hasNotInjected(final Constructor<?> injectedConstructor) {
        return isNull(injectedConstructor);
    }

    private Object registerBean(final Class<?> clazz) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
        Object bean = BeanUtils.instantiateClass(concreteClass);
        beans.put(clazz, bean);
        return bean;
    }

    private Object registerBean(final Class<?> clazz, final Constructor<?> injectedConstructor) {
        List<Object> params = Lists.newArrayList();
        for (Class<?> parameterType : injectedConstructor.getParameterTypes()) {
            Object paramBean = instantiateBean(parameterType);
            params.add(paramBean);
        }

        Object bean = BeanUtils.instantiateClass(injectedConstructor, params.toArray());
        beans.put(clazz, bean);
        return bean;
    }
}
