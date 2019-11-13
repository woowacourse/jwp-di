package nextstep.di.factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ClasspathBeanCreationResource implements BeanCreationResource {
    private Constructor constructor;

    public ClasspathBeanCreationResource(Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public Object initialize(Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new BeanCreateException(e);
        }
    }

    @Override
    public Class<?> getType() {
        return constructor.getDeclaringClass();
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return Arrays.asList(constructor.getParameterTypes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClasspathBeanCreationResource that = (ClasspathBeanCreationResource) o;
        return constructor.equals(that.constructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constructor);
    }
}
