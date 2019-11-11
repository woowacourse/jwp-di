package nextstep.di.factory;

import java.lang.reflect.Method;

public class MethodBeanDefinition {
    private Object implementation;
    private Class<?> beanClass;
    private Method method;

    public MethodBeanDefinition(Object implementation, Class<?> beanClass, Method method) {
        this.implementation = implementation;
        this.beanClass = beanClass;
        this.method = method;
    }

    public Object getImplementation() {
        return implementation;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public Method getMethod() {
        return method;
    }
}
