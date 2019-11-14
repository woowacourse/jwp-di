package nextstep.di.factory.domain;

import nextstep.di.factory.domain.beandefinition.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);
    Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation);
    void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);
    void initialize();
    void addInstantiateBeans(Set<Class<?>> preInstantiateBeans);
}
