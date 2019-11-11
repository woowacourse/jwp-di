package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.exception.BeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class DefaultBeanDefinition implements BeanDefinition {

    private final Class<?> beanClass;
    private final Constructor<?> constructor;

    public DefaultBeanDefinition(final Class<?> beanClass) {
        this.beanClass = beanClass;
        Optional<Constructor<?>> maybeInjectedConstructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        this.constructor = maybeInjectedConstructor.orElseGet(() -> {
            try {
                return beanClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new BeanDefinitionException(e);
            }
        });
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object createBean(final Object[] parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanDefinitionException(e);
        }
    }

    @Override
    public String toString() {
        return "beanClass=" + beanClass +
                ", constructor=" + constructor;
    }
}
