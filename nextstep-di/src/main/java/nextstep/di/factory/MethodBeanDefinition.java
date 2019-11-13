package nextstep.di.factory;

import nextstep.exception.MethodBeanDefinitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(MethodBeanDefinition.class);

    private final Method method;
    private final Class<?> returnType;
    private final Object instance;

    public MethodBeanDefinition(Method method, Class<?> returnType, Object instance) {
        this.method = method;
        this.returnType = returnType;
        this.instance = instance;
    }

    @Override
    public Class<?> getBeanClass() {
        return returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public Object createBean(Object[] parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new MethodBeanDefinitionException(e);
        }
    }
}
