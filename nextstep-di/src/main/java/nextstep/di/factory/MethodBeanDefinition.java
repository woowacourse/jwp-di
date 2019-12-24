package nextstep.di.factory;

import nextstep.di.factory.exception.CannotCreateInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(MethodBeanDefinition.class);

    private final Method method;

    public MethodBeanDefinition(Method method) {
        this.method = method;
    }

    @Override
    public Object instantiate(final Object... parameter) {
        try {
            Object instance = method.getDeclaringClass().getConstructor().newInstance();

            return method.invoke(instance, parameter);
        } catch (Exception e) {
            logger.error(">> instantiate", e);
            throw new CannotCreateInstance(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
}
