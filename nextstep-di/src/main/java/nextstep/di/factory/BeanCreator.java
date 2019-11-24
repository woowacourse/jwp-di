package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface BeanCreator {

    Object createBean(Object concreteObject, Object... parameters)
            throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
