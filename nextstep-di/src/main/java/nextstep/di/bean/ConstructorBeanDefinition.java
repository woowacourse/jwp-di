package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.ReflectionUtils;

import java.lang.reflect.Constructor;

public class ConstructorBeanDefinition implements BeanDefinition {
    private final Class<?> clazz;
    private final Constructor<?> constructor;

    public ConstructorBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
        this.constructor = initConstructor(clazz);
    }

    private Constructor initConstructor(Class<?> clazz) {
        Constructor injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        return injectedConstructor == null ? ReflectionUtils.getDefaultConstructor(clazz) : injectedConstructor;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object instantiate(Object... parameters) {
        return ReflectionUtils.newInstance(constructor, parameters);
    }
}
