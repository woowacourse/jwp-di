package nextstep.di.factory.domain;

import java.lang.annotation.Annotation;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);
    Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation);
    void addBean(Class<?> clazz, Object instance);
}
