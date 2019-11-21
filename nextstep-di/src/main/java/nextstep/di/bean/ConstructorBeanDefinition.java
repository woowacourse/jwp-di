package nextstep.di.bean;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.ReflectionUtils;

import java.lang.reflect.Constructor;

public class ConstructorBeanDefinition<T> implements BeanDefinition<T> {
    private final Class<T> clazz;
    private final Constructor<T> constructor;

    public ConstructorBeanDefinition(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = initConstructor(clazz);
    }

    private Constructor<T> initConstructor(Class<T> clazz) {
        Constructor<T> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        return injectedConstructor == null ? ReflectionUtils.getDefaultConstructor(clazz) : injectedConstructor;
    }

    @Override
    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public T instantiate(Object... parameters) {
        return ReflectionUtils.newInstance(constructor, parameters);
    }
}
