package nextstep.di.factory.domain;

import nextstep.di.factory.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationBean implements BeanDefinition {
    private Method method;
    private List<BeanDefinition> parameters;

    public ConfigurationBean(Method method) {
        this.method = method;
        parameters = new ArrayList<>();
        for (Class<?> clazz : method.getParameterTypes()) {
            parameters.add(new ParameterBeanDefinition(clazz));
        }
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
    public List<BeanDefinition> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getBeanType() {
        return method.getReturnType();
    }
}
