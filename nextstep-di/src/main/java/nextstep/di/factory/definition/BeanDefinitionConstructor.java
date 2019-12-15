package nextstep.di.factory.definition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BeanDefinitionConstructor implements BeanDefinition {
    private final Constructor<?> constructor;

    public BeanDefinitionConstructor(final Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object createBean(Object... objects) {
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
