package nextstep.di.factory.domain;

import java.util.List;

public class ParameterBeanDefinition implements BeanDefinition {
    private Class<?> clazz;

    public ParameterBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean hasParameter() {
        return false;
    }

    //TODO IllegalArgumentException 발생
    @Override
    public Object makeInstance(Object... parameters) {
        throw new IllegalArgumentException();
    }

    @Override
    public Class<?> getBeanType() {
        return this.clazz;
    }

    @Override
    public List<BeanDefinition> getParameters() {
        throw new IllegalArgumentException();
    }
}
