package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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

    @Override
    public Object instantiate(BeanFactory beanFactory) {
        Object[] parameterBeans = Arrays.stream(method.getParameterTypes())
                .map(beanFactory::getBean)
                .toArray();
        try {
            return method.invoke(implementation, parameterBeans);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
