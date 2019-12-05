package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface BeanFactory {
    <T> T getBean(Class<T> requiredType);

    void initialize();

    Map<Class<?>, Object> getBeans(Class<? extends Annotation> annotation);
}
