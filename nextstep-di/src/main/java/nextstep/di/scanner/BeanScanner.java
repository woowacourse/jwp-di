package nextstep.di.scanner;

import nextstep.di.BeanInitiator;

import java.util.Set;

public interface BeanScanner {
    Set<Class<?>> getInstantiatedTypes();

    boolean isContainsBean(Class<?> clazz);

    BeanInitiator getBeanInitiator(Class<?> clazz);
}
