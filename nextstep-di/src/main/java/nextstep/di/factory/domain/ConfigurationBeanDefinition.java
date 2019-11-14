package nextstep.di.factory.domain;

import nextstep.di.factory.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private Method method;
    private List<Class<?>> parameters;

    public ConfigurationBeanDefinition(Method method) {
        this.method = method;
        parameters = new ArrayList<>();
        parameters.addAll(Arrays.asList(method.getParameterTypes()));
    }

    @Override
    public boolean hasParameter() {
        return this.parameters.size() > 0;
    }

    @Override
    public Object makeInstance(Object... parameters) {
        return ReflectionUtils.invoke(method, method.getDeclaringClass(), parameters);
    }

    @Override
    public List<Class<?>> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getBeanType() {
        return method.getReturnType();
    }
}
