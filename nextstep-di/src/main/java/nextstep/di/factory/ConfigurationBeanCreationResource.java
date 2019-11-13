package nextstep.di.factory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigurationBeanCreationResource implements BeanCreationResource {
    private Method method;
    private Object configurationObject;

    ConfigurationBeanCreationResource(Method method, Object configurationObject) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationBeanCreationResource that = (ConfigurationBeanCreationResource) o;
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method);
    }
}
