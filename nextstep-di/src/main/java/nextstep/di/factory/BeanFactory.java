package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private Map<Class<?>, BeanDefinition> definitions;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Map<Class<?>, BeanDefinition> definitions) {
        this.definitions = definitions;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Class<?>> getControllers() {
        return beans.keySet().stream()
                .filter(bean -> bean.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet());
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : definitions.values()) {
            beans.put(beanDefinition.getType(), createBean(beanDefinition));
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> configType = beanDefinition.getConfigType();
        Object configuration = createConfiguration(configType);
        List<Object> parameters = getParameterBeans(beanDefinition);

        try {
            BeanCreator beanCreator = beanDefinition.getBeanCreator();
            return beanCreator.createBean(configuration, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new BeanCreationFailException(e);
        }
    }

    private Object createConfiguration(Class<?> typeOfConfig) {
        if (isNotInCreatedBeans(typeOfConfig)) {
            BeanDefinition beanDefinition = definitions.get(typeOfConfig);
            beans.put(beanDefinition.getType(), createBean(beanDefinition));
        }

        return beans.get(typeOfConfig);
    }

    private boolean isNotInCreatedBeans(Class<?> configType) {
        return (configType != null) && (beans.get(configType) == null);
    }

    private List<Object> getParameterBeans(BeanDefinition beanDefinition) {
        List<Object> parameters = new ArrayList<>();

        beanDefinition.getParameters()
                .forEach(parameter -> parameters.add(getParameterBean(parameter)));
        return parameters;
    }

    private Object getParameterBean(Class<?> parameter) {
        if (beans.containsKey(parameter)) {
            return beans.get(parameter);
        }

        BeanDefinition beanDefinition = BeanFactoryUtils.findBeanDefinition(parameter, definitions);
        return createBean(beanDefinition);
    }
}
