package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;

public interface BeanConstructor {
    Class[] getParameterTypes();
    Class<?> getReturnType();
    Object construct(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
