package nextstep.di.factory.beandefinition;

import org.reflections.ReflectionsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanDefinition.class);

    private final Method method;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;
    private final Object instance;

    public ConfigurationBeanDefinition(Method method) {
        this.method = method;
        this.returnType = method.getReturnType();
        this.parameterTypes = method.getParameterTypes();
        this.instance = getInstanceFrom(method);
    }

    private Object getInstanceFrom(Method method) {
        try {
            return method.getDeclaringClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Cannot instantiate by configuration class' bean method : {}", e.getMessage());
            throw new ReflectionsException(e.getMessage(), e);
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
