package nextstep.di.factory;

import java.util.List;

public class BeanDefinition {
    private Class<?> type;
    private Class<?> configType;
    private BeanCreator beanCreator;
    private List<Class<?>> parameters;

    public BeanDefinition(Class<?> type, Class<?> configType, BeanCreator beanCreator, List<Class<?>> parameters) {
        this.type = type;
        this.configType = configType;
        this.beanCreator = beanCreator;
        this.parameters = parameters;
    }

    public Class<?> getType() {
        return type;
    }

    public Class<?> getConfigType() {
        return configType;
    }

    public BeanCreator getBeanCreator() {
        return beanCreator;
    }

    public List<Class<?>> getParameters() {
        return parameters;
    }
}
