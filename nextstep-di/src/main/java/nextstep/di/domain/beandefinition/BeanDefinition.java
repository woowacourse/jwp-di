package nextstep.di.domain.beandefinition;

import java.util.List;

public interface BeanDefinition {
    int ZERO = 0;

    Object makeInstance(Object... parameters);

    Class<?> getBeanType();

    List<Class<?>> getParameters();

    default boolean hasParameter() {
        return getParameters().size() > ZERO;
    }
}
