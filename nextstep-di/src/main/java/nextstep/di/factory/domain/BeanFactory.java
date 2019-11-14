package nextstep.di.factory.domain;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);
    Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation);
    void addBean(Class<?> clazz, Object instance);
    void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);
    void initialize();
}
