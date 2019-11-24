package nextstep.di.bean;

import java.util.*;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();

    @Override
    public void register(BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getBeanClass(), beanDefinition);
    }

    @Override
    public void register(List<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(this::register);
    }

    public Collection<BeanDefinition> getBeanDefinitions() {
        return Collections.unmodifiableCollection(beanDefinitions.values());
    }

    @Override
    public BeanDefinition get(Class<?> classType) {
        return beanDefinitions.get(classType);
    }
}
