package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DefaultBeanDefinition extends BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(DefaultBeanDefinition.class);

    public DefaultBeanDefinition(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public Object instantiate(BeanFactory beanFactory) {
        Class<?> beanClass = getBeanClass();
        if (beanClass.isInterface()) {
            return beanFactory.getBean(beanClass);
        }

        try {
            Constructor<?> constructor = getConstructor(beanClass);
            return createInstance(constructor, beanFactory);
        } catch (Exception e) {
            log.error("Bean create Fail : ", e);
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructor(Class<?> beanClass) throws NoSuchMethodException {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        if (injectedConstructor == null) {
            return beanClass.getDeclaredConstructor();
        }
        return injectedConstructor;
    }

    private Object createInstance(Constructor<?> constructor, BeanFactory beanFactory) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameters.add(beanFactory.getBean(parameterType));
        }
        return constructor.newInstance(parameters.toArray());
    }
}
