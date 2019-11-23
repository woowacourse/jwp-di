package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {

    private final Method method;
    private final Object instance;

    public MethodBeanDefinition(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public Class[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public Object instantiate(Object... parameters) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, parameters);
    }

    @Override
    public String toString() {
        return "MethodBeanConstructor{" +
                "method=" + method +
                ", instance=" + instance +
                '}';
    }
}
