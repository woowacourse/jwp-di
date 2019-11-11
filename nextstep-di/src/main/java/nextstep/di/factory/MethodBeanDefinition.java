package nextstep.di.factory;

import java.lang.reflect.Method;

public class MethodBeanDefinition extends BeanDefinition {
    private Object implementation;
    private Method method;

    public MethodBeanDefinition(Object implementation, Class<?> beanClass, Method method) {
        super(beanClass);
        this.implementation = implementation;
        this.method = method;
    }

    public Object getImplementation() {
        return implementation;
    }

    public Method getMethod() {
        return method;
    }
}
