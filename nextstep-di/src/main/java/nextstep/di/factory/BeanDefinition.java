package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public interface BeanDefinition {

    Class[] getParameterTypes();

    Class<?> getReturnType();

    Object instantiate(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    default boolean hasReference(BeanDefinition beanDefinition) {
        List<Class<?>> parameterTypes = Arrays.asList(getParameterTypes());
        return parameterTypes.contains(beanDefinition.getReturnType());
    }
}
