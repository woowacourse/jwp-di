package nextstep.di.factory.domain;

import nextstep.di.factory.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.List;

//@TODO AnnotationBean
public class ConstructorBean implements BeanDefinition {
    private final Constructor<?> constructor;
    private final List<BeanDefinition> parameters;
    private Class<?> clazz;

    public ConstructorBean(Class<?> clazz, Constructor<?> constructor, List<BeanDefinition> parameters) {
        this.clazz = clazz;
        this.constructor = constructor;
        this.parameters = parameters;
    }

    public boolean hasParameter() {
        return parameters.size() > 0;
    }

    public Object makeInstance(Object... parameters) {
        return ReflectionUtils.newInstance(constructor, parameters);
    }

    public List<BeanDefinition> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getBeanType() {
        return clazz;
    }
}
