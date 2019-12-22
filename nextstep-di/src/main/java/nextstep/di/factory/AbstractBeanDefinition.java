package nextstep.di.factory;

import java.util.List;

public abstract class AbstractBeanDefinition implements BeanDefinition {
    private Class<?> type;
    private BeanCreator beanCreator;
    private List<Class<?>> parameters;

    public AbstractBeanDefinition(Class<?> type, BeanCreator beanCreator, List<Class<?>> parameters) {
        this.type = type;
        this.beanCreator = beanCreator;
        this.parameters = parameters;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Class<?> getConfigType() {
        return null;
    }

    @Override
    public BeanCreator getBeanCreator() {
        return beanCreator;
    }

    @Override
    public List<Class<?>> getParameters() {
        return parameters;
    }
}
