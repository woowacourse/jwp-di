package nextstep.di.definition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private Object instanceOfConfigClass;
    private Method method;

    public ConfigurationBeanDefinition(Object instanceOfConfigClass, Method method) {
        this.instanceOfConfigClass = instanceOfConfigClass;
        this.method = method;
    }

    @Override
    public Object instantiate(final Object... parameters) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instanceOfConfigClass, parameters);
    }

    @Override
    public Class<?> getClassType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
}
