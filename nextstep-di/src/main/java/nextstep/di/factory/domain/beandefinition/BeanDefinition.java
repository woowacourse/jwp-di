package nextstep.di.factory.domain.beandefinition;

import java.util.List;

public interface BeanDefinition {
    int ZERO = 0;
    boolean hasParameter();
    Object makeInstance(Object... parameters);
    Class<?> getBeanType();
    List<Class<?>> getParameters();
}
