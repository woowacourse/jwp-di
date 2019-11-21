package nextstep.di.factory.definition;

import nextstep.di.factory.BeanFactoryUtils;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class ConstructorBeanDefinition implements BeanDefinition {

    private Constructor constructor;

    public ConstructorBeanDefinition(Class<?> clazz) {
        this.constructor = BeanFactoryUtils.findConstructor(clazz);
    }

    @Override
    public Class<?> getName() {
        return constructor.getDeclaringClass();
    }

    @Override
    public Class<?>[] getParams() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object createBean(Object... initArgs) throws Exception {
        return constructor.newInstance(initArgs);
    }

    @Override
    public boolean matchClass(Class<?> clazz) {
        return constructor.getDeclaringClass().equals(clazz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorBeanDefinition that = (ConstructorBeanDefinition) o;
        return Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructor);
    }

    @Override
    public String toString() {
        return "ConstructorBeanDefinition{" +
                "constructor=" + constructor +
                '}';
    }
}
