package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.exception.DefaultBeanDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class DefaultBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanDefinition.class);

    private final Class<?> beanClass;
    private final Constructor<?> constructor;

    public DefaultBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        Optional<Constructor<?>> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        this.constructor = injectedConstructor.orElseGet(() -> {
            try {
                return beanClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new DefaultBeanDefinitionException(e);
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
    public Object createBean(Object[] parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new DefaultBeanDefinitionException(e);
        }
    }
}
