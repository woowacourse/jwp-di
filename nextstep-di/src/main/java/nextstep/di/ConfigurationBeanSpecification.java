package nextstep.di;

import nextstep.di.factory.exception.InstantiationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ConfigurationBeanSpecification implements BeanSpecification {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanSpecification.class);
    private final Method method;
    private final Object instance;

    public ConfigurationBeanSpecification(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public Object instantiate(Object... parameter) {
        try {
            return method.invoke(instance, parameter);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Instantiation failed", e);
            throw new InstantiationFailedException("Instantiation Failed", e);
        }
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public boolean canInterface() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationBeanSpecification that = (ConfigurationBeanSpecification) o;
        return Objects.equals(method, that.method) &&
                Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, instance);
    }
}
