package nextstep.di.factory;

import nextstep.di.initiator.BeanInitiator;

import java.util.Set;
import java.util.function.Predicate;

public interface BeanFactory {
    void addBeanInitiator(Class<?> clazz, BeanInitiator beanInitiator);

    @SuppressWarnings("unchecked")
    <T> T getBean(Class<T> requiredType);

    Set<Class<?>> getBeanTypes(Predicate<Class<?>> predicate);
}
