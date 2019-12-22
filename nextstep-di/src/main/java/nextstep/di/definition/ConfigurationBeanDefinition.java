package nextstep.di.definition;

import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationBeanDefinition.class);

    private final Method method;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;
    private final Object instance;

    public ConfigurationBeanDefinition(final Method method) {
        this.method = method;
        this.returnType = method.getReturnType();
        this.parameterTypes = method.getParameterTypes();
        this.instance = getInstanceFrom(method);
    }

    private Object getInstanceFrom(final Method method) {
        try {
            return method.getDeclaringClass().getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            LOG.error("Cannot instantiate by configuration class's bean method : {}", e.getMessage());
            throw new ReflectionsException(e);
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object instantiate(Object[] parameters) {
        return ReflectionUtils.invokeMethod(method, instance, parameters);
    }

}
