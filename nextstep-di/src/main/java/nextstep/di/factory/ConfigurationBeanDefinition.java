package nextstep.di.factory;

import java.util.List;

public class ConfigurationBeanDefinition extends AbstractBeanDefinition {
    private Class<?> configType;

    ConfigurationBeanDefinition(Class<?> type, Class<?> configType, BeanCreator beanCreator, List<Class<?>> parameters) {
        super(type, beanCreator, parameters);
        this.configType = configType;
    }

    @Override
    public Class<?> getConfigType() {
        return configType;
    }
}
