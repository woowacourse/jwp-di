package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanBox implements BeanBox<Method> {

    private Method method;

    public MethodBeanBox(Method method) {
        this.method = method;
    }

    @Override
    public boolean hasParams() {
        return method.getParameterCount() != 0;
    }

    @Override
    public Method getInvoker() {
        return method;
    }

    @Override
    public Object instantiate() {
        try {
            Object instance = method.getDeclaringClass().newInstance();
            return method.invoke(instance);
        } catch (IllegalAccessException
                | InstantiationException
                | InvocationTargetException e) {
            throw new MethodBeanBoxInvokeFailException();
        }
    }

    @Override
    public Object putParameterizedObject(Class<?> preInstanticateBean, Object[] params) {
        try {
            Object instance = method.getDeclaringClass().newInstance();
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("메서드 초기화 실패");
        }
    }

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }
}
