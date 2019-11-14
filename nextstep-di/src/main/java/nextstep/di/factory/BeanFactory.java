package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

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
        return beans.keySet()
                .stream()
                .filter(bean -> bean.isAnnotationPresent(Controller.class))
                .collect(Collectors.toSet())
                ;
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : definitions.values()) {
            beans.put(beanDefinition.getType(), createBean(beanDefinition));
        }
    }

    private Object createBean(BeanDefinition beanDefinition) {
        List<Object> parameters = getParameterBeans(beanDefinition);

        return instantiateBean(beanDefinition, parameters);
    }

    private List<Object> getParameterBeans(BeanDefinition beanDefinition) {
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameter : beanDefinition.getParameters()) {
            Object bean = beans.get(parameter);
            if (!beans.containsKey(parameter)) {
                BeanDefinition parameterBeanDefinition = BeanFactoryUtils.findBeanDefinition(parameter, definitions);
                bean = createBean(parameterBeanDefinition);
            }

            parameters.add(bean);
        }

        return parameters;
    }

    private Object instantiateBean(BeanDefinition beanDefinition, List<Object> parameters) {
        Class<?> configType = beanDefinition.getConfigType();
        Object configurationBean = createConfigurationBean(configType);

        try {
            return beanDefinition.getBeanCreator().create(configurationBean, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

    private Object createConfigurationBean(Class<?> configType) {
        if (configType != null && beans.get(configType) == null) {
            BeanDefinition beanDefinition = definitions.get(configType);
            beans.put(beanDefinition.getType(), createBean(beanDefinition));
        }

        return beans.get(configType);
    }
}
