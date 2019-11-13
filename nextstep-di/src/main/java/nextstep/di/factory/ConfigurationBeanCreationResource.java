package nextstep.di.factory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ConfigurationBeanCreationResource implements BeanCreationResource {
    private Method method;
    private Object configurationObject;

    public ConfigurationBeanCreationResource(Method method, Object configurationObject) {
        this.method = method;
        this.configurationObject = configurationObject;
    }

    @Override
    public Object initialize(Object... params) {
        try {
            return method.invoke(configurationObject, params);
        } catch (Exception e) {
            throw new BeanCreateException(e);
        }
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return Arrays.asList(method.getParameterTypes());
    }
}
