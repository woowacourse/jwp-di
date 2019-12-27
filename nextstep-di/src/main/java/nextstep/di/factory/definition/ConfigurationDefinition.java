package nextstep.di.factory.definition;

import nextstep.exception.BeanCreateException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationDefinition implements BeanDefinition {
    private Method beanMethod;
    private Object instance;

    public ConfigurationDefinition(Method beanMethod, Object instance) {
        this.beanMethod = beanMethod;
        this.instance = instance;
    }

    @Override
    public Object generateBean(Object... params) {
        try {
            return beanMethod.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanCreateException();
        }
    }

    @Override
    public Class<?> getType() {
        return this.beanMethod.getReturnType();
    }

    @Override
    public Class<?>[] getParams() {
        return beanMethod.getParameterTypes();
    }
}
