package nextstep.di.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationBean implements BeanDefinition {
    private Object clazz;
    private Method method;

    public ConfigurationBean(Object clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public Object getInstance(Object[] params) {
        try {
            return method.invoke(clazz, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Class[] getParameterTypes() {
        return this.method.getParameterTypes();
    }
}
