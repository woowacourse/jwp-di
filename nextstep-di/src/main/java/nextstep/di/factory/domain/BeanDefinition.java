package nextstep.di.factory.domain;

import java.util.List;

public interface BeanDefinition {
    boolean hasParameter();
    Object makeInstance(Object... parameters);
    List<BeanDefinition> getParameters();
}
