package nextstep.di.factory.domain;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ApplicationContext {
    void initialize();
    <T> T getBean(Class<T> requiredType);
    Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation);
}
