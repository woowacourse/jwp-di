package nextstep.di.bean;

import java.util.Collection;
import java.util.List;

public interface BeanDefinitionRegistry {
    void register(final BeanDefinition beanDefinition);

    void register(final List<BeanDefinition> beanDefinitions);

    Collection<BeanDefinition> getBeanDefinitions();

    BeanDefinition get(final Class<?> classType);
}
