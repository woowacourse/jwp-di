package nextstep.di;

import nextstep.di.factory.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class AnnotatedTypeBeanSpecification implements BeanSpecification {
    private final Constructor<?> constructor;

    public AnnotatedTypeBeanSpecification(Class<?> clazz) {
        this.constructor = getConstructorOf(clazz);
    }

    private Constructor<?> getConstructorOf(final Class<?> preInstantiateBean) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (Objects.isNull(constructor)) {
            return BeanFactoryUtils.getDefaultConstructor(preInstantiateBean);
        }
        constructor.setAccessible(true);
        return constructor;
    }

    @Override
    public Object instantiate(Object... parameter) {
        return BeanFactoryUtils.instantiate(constructor, parameter);
    }

    @Override
    public Class<?> getType() {
        return constructor.getDeclaringClass();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }
}
