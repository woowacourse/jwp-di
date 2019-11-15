package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);

    Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation);
}
