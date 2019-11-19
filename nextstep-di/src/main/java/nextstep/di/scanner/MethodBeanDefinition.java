package nextstep.di.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition {
    private Object declaredObject;
    private Method method;

    public MethodBeanDefinition(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public Class<?> getType() {
        return method.getReturnType();
    }

    public Class<?>[] getParams() {
        return method.getParameterTypes();
    }

    public Object createBean(Object... params) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(declaredObject, params);
    }
}
