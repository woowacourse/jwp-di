package nextstep.di.factory.beandefinition;

import nextstep.exception.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationBeanDefinition.class);

    private final Object object;
    private final Class<?> classType;
    private final Method method;

    public ConfigurationBeanDefinition(Object object, Class<?> classType, Method method) {
        this.object = object;
        this.classType = classType;
        this.method = method;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object instantiate(Object... parameters) {
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new BeanCreationException(e);
        }
    }
}
