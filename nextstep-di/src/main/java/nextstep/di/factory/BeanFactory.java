package nextstep.di.factory;

import java.util.Set;
import java.util.function.Predicate;

public interface BeanFactory {
    @SuppressWarnings("unchecked")
    <T> T getBean(Class<T> requiredType);

    Set<Class<?>> getBeanTypes(Predicate<Class<?>> predicate);
}
