package nextstep.di.beandefinition;

import nextstep.di.factory.BeanFactory;

import java.util.Set;

public interface BeanDefinition {
    Class<?> getBeanType();

    Set<Class<?>> getDependantTypes();

    Object create(BeanFactory beanFactory);
}
