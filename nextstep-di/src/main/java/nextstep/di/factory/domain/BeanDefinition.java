package nextstep.di.factory.domain;

import java.util.List;

public interface BeanDefinition {
    boolean hasParameter();
    Object makeInstance(Object... parameters);
    Class<?> getBeanType();
    List<Class<?>> getParameters();
}
