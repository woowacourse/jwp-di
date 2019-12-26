package nextstep.di;

import nextstep.di.factory.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class AnnotatedTypeBeanSpecification implements BeanSpecification {
    private final Constructor<?> constructor;

    public AnnotatedTypeBeanSpecification(Class<?> clazz) {
        this.constructor = getConstructorOf(clazz);
        this.constructor.setAccessible(true);
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

    @Override
    public boolean canInterface() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnnotatedTypeBeanSpecification that = (AnnotatedTypeBeanSpecification) o;
        return Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructor);
    }
}
