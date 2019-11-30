package nextstep.di.bean;

import nextstep.di.factory.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private final Class<?> returnType;
    private final Method method;
    private final Object configClassInstance;

    public ConfigurationBeanDefinition(Object configClassInstance, Method method) {
        this.returnType = method.getReturnType();
        this.method = method;
        this.configClassInstance = configClassInstance;
    }

    @Override
    public Class<?> getClazz() {
        return this.returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object instantiate(Object... parameters) {
        return ReflectionUtils.invoke(method, configClassInstance, parameters);
    }
}
