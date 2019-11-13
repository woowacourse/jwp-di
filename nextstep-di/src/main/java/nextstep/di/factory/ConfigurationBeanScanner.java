package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.CannotCreateInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ConfigurationBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Configuration.class)) {
            beanFactory.instantiate(clazz);
            registerBeanMethods(clazz, clazz.getDeclaredMethods());
        }
    }

    private void registerBeanMethods(final Class<?> clazz, final Method[] methods) {
        for (Method method : methods) {
            registerBeanMethod(clazz, method);
        }
    }

    private void registerBeanMethod(final Class<?> clazz, final Method method) {
        if (method.isAnnotationPresent(Bean.class)) {
            registerBean(clazz, method);
        }
    }

    private void registerBean(final Class<?> clazz, final Method method) {
        try {
            beanFactory.addBean(method.getReturnType(), method.invoke(beanFactory.getBean(clazz)));
        } catch (Exception e) {
            logger.error(">> registerBean", e);
            throw new CannotCreateInstance(e);
        }
    }
}
