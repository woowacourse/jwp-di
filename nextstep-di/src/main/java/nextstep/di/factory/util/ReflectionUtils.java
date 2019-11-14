package nextstep.di.factory.util;

import nextstep.di.factory.exception.ReflectionUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new ReflectionUtilException(e.getMessage(), e);
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new ReflectionUtilException(e.getMessage(), e);
        }
    }

    public static Object invoke(Method method, Class<?> owner, Object... objects) {
        try {
            return method.invoke(newInstance(owner), objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new ReflectionUtilException(e.getMessage(), e);
        }
    }

    /**
     * 생성자가 있으면 해당하는 생성자를 아니면 기본 생성자를 반환한다
     */
    public static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length > 0) {
                return constructor;
            }
        }

        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new ReflectionUtilException(e.getMessage(), e);
        }
    }
}
