package nextstep.di.factory.domain;

import nextstep.di.factory.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class ConfigurationBean implements BeanDefinition {
    private Method method;
    private List<BeanDefinition> parameters;

    public ConfigurationBean(Method method) {
        this.method = method;

    }

    @Override
    public boolean hasParameter() {
        return parameters.size() > 0;
    }

    @Override
    public Object makeInstance(Object... parameters) {
        return ReflectionUtils.invoke(method, method.getDeclaringClass(), parameters);
    }

    @Override
    public List<BeanDefinition> getParameters() {
        return null;
    }
}
