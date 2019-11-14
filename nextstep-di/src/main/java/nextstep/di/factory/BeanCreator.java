package nextstep.di.factory;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface BeanCreator {

    Object create(Object concreteObject, Object... objects) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
