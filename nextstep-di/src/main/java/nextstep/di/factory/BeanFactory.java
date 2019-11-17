package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface BeanFactory {
    void initialize();

    <T> T getBean(Class<T> requiredType);

    Set<Class<?>> getTypes(Class<? extends Annotation> annotation);
}
