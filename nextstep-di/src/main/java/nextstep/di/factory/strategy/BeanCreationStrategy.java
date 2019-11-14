package nextstep.di.factory.strategy;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public interface BeanCreationStrategy {
    boolean canHandle(Class<?> clazz);

    List<Class<?>> getDependencyTypes(Class<?> clazz);

    Object createBean(Class<?> clazz, List<Object> parameterInstances, Map<Class<?>, Object> beans) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException;
}
