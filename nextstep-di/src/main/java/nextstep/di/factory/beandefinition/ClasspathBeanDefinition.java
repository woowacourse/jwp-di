package nextstep.di.factory.beandefinition;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.exception.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClasspathBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathBeanDefinition.class);

    private final Class<?> classType;
    private final Constructor<?> constructor;

    public ClasspathBeanDefinition(Class<?> classType) {
        this.classType = classType;
        this.constructor = getConstructor();
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        if (constructor != null) {
            return constructor.getParameterTypes();
        }
        return null;
    }

    @Override
    public Object instantiate(Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }

    private Constructor<?> getConstructor() {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(classType);
        if (constructor != null) {
            return constructor;
        }
        return getDefaultConstructor();
    }

    private Constructor<?> getDefaultConstructor() {
        try {
            return classType.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }
}
