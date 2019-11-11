package nextstep.di.factory.support;

import nextstep.di.factory.exception.ReflectionUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
}
