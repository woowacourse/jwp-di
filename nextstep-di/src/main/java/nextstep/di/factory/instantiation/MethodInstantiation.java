package nextstep.di.factory.instantiation;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.exception.BeanCreateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInstantiation implements InstantiationMethod {
    private static final Logger logger = LoggerFactory.getLogger(MethodInstantiation.class);

    private final Method method;
    private final Object instanceWithConfigureAnnotation;

    public MethodInstantiation(Method method, Object instanceWithConfigureAnnotation) {
        this.method = method;
        this.instanceWithConfigureAnnotation = instanceWithConfigureAnnotation;
    }

    @Override
    public Object getInstance(BeanCreateMatcher beanCreateMatcher) {
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] objects = Arrays.stream(parameterTypes)
                    .map(parameterType -> beanCreateMatcher.get(parameterType).getInstance(beanCreateMatcher))
                    .toArray();
            return method.invoke(instanceWithConfigureAnnotation, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("error : ", e);
            throw new BeanCreateException(e);
        }
    }
}
