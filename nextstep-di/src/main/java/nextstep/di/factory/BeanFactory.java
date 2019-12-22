package nextstep.di.factory;

import nextstep.di.definition.BeanDefinition;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanFactory {

    private final Map<Class<?>, BeanDefinition> beanDefinitionRegistry = new HashMap<>();
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(final Set<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(beanDefinition ->
                beanDefinitionRegistry.putIfAbsent(beanDefinition.getBeanClass(), beanDefinition));
    }

    public void initialize() {
        for (final Class<?> beanDefinitionClass : beanDefinitionRegistry.keySet()) {
            beans.putIfAbsent(beanDefinitionClass, instantiateBean(beanDefinitionClass));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiateBean(final Class<?> beanClass) {
        return (T) beans.getOrDefault(beanClass, instantiate(beanClass));
    }

    private Object instantiate(final Class<?> beanClass) {
        final BeanDefinition beanDefinition = beanDefinitionRegistry.get(beanClass);
        return beanDefinition.instantiate(getParameters(beanDefinition));
    }

    private Object[] getParameters(final BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(this::findBeanDefinition)
                .map(BeanDefinition::getBeanClass)
                .map(this::instantiateBean)
                .toArray(Object[]::new);
    }

    private BeanDefinition findBeanDefinition(final Class<?> type) {
        final BeanDefinition beanDefinition = beanDefinitionRegistry.get(type);
        if (Objects.nonNull(beanDefinition)) {
            return beanDefinition;
        }
        final Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(type, beanDefinitionRegistry.keySet());
        return beanDefinitionRegistry.get(concreteClass);
    }

    @SuppressWarnings("unchecked")
    public Map<Class<?>, Object> getAnnotatedClasses(final Class clazz) {
        return beans.keySet().stream()
                .filter(aClass -> aClass.isAnnotationPresent(clazz))
                .collect(Collectors.toMap(Function.identity(), this::getBean));
    }

}
