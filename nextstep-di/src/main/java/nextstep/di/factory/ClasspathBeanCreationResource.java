package nextstep.di.factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

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
}
