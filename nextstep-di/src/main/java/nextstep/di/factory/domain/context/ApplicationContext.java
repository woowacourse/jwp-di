package nextstep.di.factory.domain.context;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ApplicationContext {
    void initialize();
    <T> T getBean(Class<T> requiredType);
    Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation);
}
