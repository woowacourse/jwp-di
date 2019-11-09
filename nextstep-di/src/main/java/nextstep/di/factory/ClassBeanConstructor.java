package nextstep.di.factory;

import nextstep.di.factory.exception.BeanFactoryInitializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ClassBeanConstructor implements BeanConstructor {
    private static final Logger logger = LoggerFactory.getLogger(ClassBeanConstructor.class);

    private final Constructor<?> constructor;

    private ClassBeanConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public static ClassBeanConstructor of (Class<?> clazz) {
        return new ClassBeanConstructor(getConstructor(clazz));
    }

    private static Constructor<?> getConstructor(Class<?> clazz) {
        try {
            Constructor<?> ctor = BeanFactoryUtils.getInjectedConstructor(clazz);
            if (Objects.isNull(ctor)) {
                return clazz.getConstructor();
            }
            return ctor;
        } catch (NoSuchMethodException e) {
            logger.error("Error while getting constructor", e);
            throw new BeanFactoryInitializeException(e);
        }
    }

    @Override
    public Class[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Class<?> getReturnType() {
        return constructor.getDeclaringClass();
    }

    @Override
    public Object construct(Object... parameters) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(parameters);
    }
}
