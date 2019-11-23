package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;

public interface BeanDefinition {

    Class[] getParameterTypes();

    Class<?> getReturnType();

    Object instantiate(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
