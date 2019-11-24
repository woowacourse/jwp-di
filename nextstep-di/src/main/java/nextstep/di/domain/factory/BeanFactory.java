package nextstep.di.domain.factory;

import nextstep.di.domain.beandefinition.BeanDefinition;

import java.util.Set;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);

    void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);

    void addInstantiateBeans(Set<Class<?>> preInstantiateBeans);
}
