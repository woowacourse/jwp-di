package nextstep.di.definition;

import java.lang.reflect.InvocationTargetException;

public interface BeanDefinition {
    Object instantiate(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    Class<?> getClassType();

    Class<?>[] getParameterTypes();
}
