package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.beandefinition.BeanDefinition;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {

    private final Map<Class<?>, BeanDefinition> beanDefinitionRegistry = Maps.newHashMap();
    private final Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(beanDefinition -> beanDefinitionRegistry.putIfAbsent(beanDefinition.getBeanClass(), beanDefinition));
    }

    public void initialize() {
        for (Class<?> beanDefinitionClass : beanDefinitionRegistry.keySet()) {
            beans.putIfAbsent(beanDefinitionClass, instantiateBean(beanDefinitionClass));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiateBean(Class<?> beanClass) {
        return (T) beans.getOrDefault(beanClass, instantiate(beanClass));
    }

    private Object instantiate(Class<?> beanClass) {
        BeanDefinition beanDefinition = beanDefinitionRegistry.get(beanClass);
        Object[] parameters = instantiateParameters(beanDefinition);
        return beanDefinition.instantiate(parameters);
    }

    private Object[] instantiateParameters(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(this::findInstantiatableBeanDefinition)
                .map(BeanDefinition::getBeanClass)
                .map(this::instantiateBean)
                .toArray(Object[]::new);
    }

    private BeanDefinition findInstantiatableBeanDefinition(Class<?> type) {
        BeanDefinition beanDefinition = beanDefinitionRegistry.get(type);
        if (beanDefinition != null) {
            return beanDefinition;
        }
        return beanDefinitionRegistry.get(BeanFactoryUtils.findConcreteClass(type, beanDefinitionRegistry.keySet()));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.keySet().stream()
                .collect(Collectors.toMap(clazz -> clazz, this::getBean));
    }

}