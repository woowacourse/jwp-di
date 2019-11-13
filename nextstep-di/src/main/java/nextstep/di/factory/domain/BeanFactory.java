package nextstep.di.factory.domain;

import nextstep.di.factory.support.Beans;
import nextstep.di.factory.util.BeanFactoryUtils;
import nextstep.di.factory.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Beans beans;

    public BeanFactory(BeanScanner beanScanner) {
        this.preInstantiateBeans = beanScanner.scan();
        this.beans = new Beans();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class clazz : preInstantiateBeans) {
            instantiateBean(clazz);
        }
    }

    private Object instantiateBean(Class<?> clazz) {
        beans.instantiate(clazz, () -> createInstance(clazz));
        return beans.get(clazz);
    }

    private Object createInstance(Class<?> clazz) {
        logger.debug("after getInstance()...");
        logger.debug("Class Type : {}", clazz);

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (injectedConstructor == null) {
            return createBean(clazz);
        }

        Object[] instances = createParameters(injectedConstructor.getParameterTypes());
        return createBeanWithParameters(injectedConstructor, instances);
    }

    private Object[] createParameters(Class<?>[] parameterTypes) {
        Object[] instances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = BeanFactoryUtils.findConcreteClass(parameterTypes[i], preInstantiateBeans);
            instances[i] = instantiateBean(parameterType);
        }
        return instances;
    }

    private Object createBean(Class<?> clazz) {
        return ReflectionUtils.newInstance(clazz);
    }

    private Object createBeanWithParameters(Constructor<?> injectedConstructor, Object[] instances) {
        return ReflectionUtils.newInstance(injectedConstructor, instances);
    }

    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return preInstantiateBeans.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }
}

