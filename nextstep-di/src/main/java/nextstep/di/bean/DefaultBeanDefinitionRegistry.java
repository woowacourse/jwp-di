package nextstep.di.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private final Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public void register(final BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getBeanClass(), beanDefinition);
    }

    @Override
    public void register(final List<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(beanDefinition ->
                this.beanDefinitions.put(beanDefinition.getBeanClass(), beanDefinition));
    }

    @Override
    public Collection<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions.values();
    }

    @Override
    public BeanDefinition get(final Class<?> classType) {
        return beanDefinitions.get(classType);
    }
}
