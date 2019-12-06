package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanNotFoundException;
import nextstep.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private BeanDefinition beanDefinition;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(final Set<Class<?>> preInstantiateComponents, final Set<Class<?>> preInstantiateConfigurationBeans) {
        this.beanDefinition = new BeanDefinition(preInstantiateComponents, preInstantiateConfigurationBeans);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        if (beans.containsKey(requiredType)) {
            return (T) beans.get(requiredType);
        }
        throw new BeanNotFoundException();
    }

    public Map<Class<?>, Object> initialize(final Class<?> configureClass) {
        ComponentFactory componentFactory = new ComponentFactory(beanDefinition);
        beans.putAll(componentFactory.initialize());

        ConfigurationBeanFactory configurationBeanFactory = new ConfigurationBeanFactory(beanDefinition, configureClass);
        beans.putAll(configurationBeanFactory.initialize(beans));

        return beans;
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.keySet()
            .stream()
            .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
            .collect(Collectors.toMap(clazz -> clazz, clazz -> beans.get(clazz)));
    }
}
