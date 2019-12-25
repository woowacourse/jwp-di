package nextstep.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class ConfigurationBeanSpecification implements BeanSpecification {
    private final Method method;
    private final Object instance;

    public ConfigurationBeanSpecification(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public Object instantiate(Object... parameter) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, parameter);
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
}
