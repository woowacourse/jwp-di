package nextstep.di.bean;

import java.util.Collection;
import java.util.List;

public interface BeanDefinitionRegistry {

    void register(BeanDefinition beanDefinition);

    void register(List<BeanDefinition> beanDefinitions);

    Collection<BeanDefinition> getBeanDefinitions();

    BeanDefinition get(Class<?> classType);
}
