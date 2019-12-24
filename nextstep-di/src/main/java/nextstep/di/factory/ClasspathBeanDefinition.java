package nextstep.di.factory;

import nextstep.di.factory.exception.CannotCreateInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ClasspathBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathBeanDefinition.class);

    private final Class<?> clazz;
    private final Constructor<?> constructor;

    public ClasspathBeanDefinition(final Class<?> clazz) {
        this.clazz = clazz;
        this.constructor = getConstructor();
    }

    private Constructor<?> getConstructor() {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (constructor != null) {
            return constructor;
        }

        return getDefaultConstructor();
    }

    private Constructor<?> getDefaultConstructor() {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new CannotCreateInstance(e);
        }
    }

    @Override
    public Object instantiate(final Object... parameter) {
        try {
            return constructor.newInstance(parameter);
        } catch (Exception e) {
            logger.error("instantiate >> ", e);
            throw new CannotCreateInstance(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }
}
