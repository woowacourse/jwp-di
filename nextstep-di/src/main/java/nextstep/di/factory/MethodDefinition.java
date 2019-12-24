package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodDefinition implements BeanDefinition {
    private final Method method;

    public MethodDefinition(final Method method) {
        this.method = method;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object createBean(Object... objects) {
        try {
            Object instance = method.getDeclaringClass().newInstance(); //todo 캐싱
            return method.invoke(instance, objects);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
