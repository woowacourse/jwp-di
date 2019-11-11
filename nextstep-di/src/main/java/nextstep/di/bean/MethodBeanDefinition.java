package nextstep.di.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {

    private final Object instance;
    private final Class<?> beanClass;
    private final Method method;

    public MethodBeanDefinition(final Object instance, final Class<?> beanClass, final Method method) {
        this.instance = instance;
        this.beanClass = beanClass;
        this.method = method;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object createBean(final Object[] parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "beanClass=" + beanClass +
                ", method=" + method;
    }
}
