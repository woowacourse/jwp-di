package nextstep.di.factory.beandefinition;

import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {

    private Class<?> returnType;

    public ConfigurationBeanDefinition(Method method) {
        this.returnType = method.getReturnType();
    }

    @Override
    public Class<?> getBeanClass() {
        return returnType;
    }
}
