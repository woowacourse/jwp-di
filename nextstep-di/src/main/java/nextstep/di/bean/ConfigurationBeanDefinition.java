package nextstep.di.bean;

import nextstep.di.factory.ReflectionUtils;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition<T> implements BeanDefinition<T> {

    private final Class<T> returnType;
    private final Method method;
    private final Object configClassInstance;

    public ConfigurationBeanDefinition(Object configClassInstance, Method method) {
        this.returnType = (Class<T>) method.getReturnType();
        this.method = method;
        this.configClassInstance = configClassInstance;
    }

    @Override
    public Class<T> getClazz() {
        return this.returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public T instantiate(Object... parameters) {
        return (T) ReflectionUtils.invoke(method, configClassInstance, parameters);
    }
}
