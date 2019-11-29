package nextstep.di.factory.beancreator;

import com.google.common.collect.Lists;
import nextstep.annotation.Inject;
import nextstep.di.factory.exception.InterfaceCannotInstantiatedException;
import nextstep.di.factory.exception.NoDefaultConstructorException;
import nextstep.di.factory.exception.BeanInstantiationFailException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ClassBeanCreator implements BeanCreator {
    private final Constructor ctor;

    public ClassBeanCreator(Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new InterfaceCannotInstantiatedException();
        }
        this.ctor = getConstructor(clazz);
    }

    @Override
    public List<Class<?>> getParams() {
        return Lists.newArrayList(ctor.getParameterTypes());
    }

    @Override
    public Object instantiate(Object... params) {
        try {
            return ctor.newInstance(params);
        } catch (Exception e) {
            throw new BeanInstantiationFailException(e);
        }
    }

    private Constructor getConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(Inject.class))
                .findFirst()
                .orElseGet(() -> getDefaultConstructor(clazz));
    }

    private Constructor<?> getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new NoDefaultConstructorException(e);
        }
    }
}
