package nextstep.di;

import nextstep.di.exception.CreateBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(MethodBeanDefinition.class);

    private Object declaredObject;
    private Method method;

    public MethodBeanDefinition(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    @Override
    public Class<?> getType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public boolean isType(Class<?> type) {
        return getType().equals(type);
    }

    @Override
    public Object createBean(Object... params) {
        try {
            return method.invoke(declaredObject, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("{} Bean instance fail. error message : {}", getType(), e.getMessage());
            throw new CreateBeanException(e);
        }
    }
}
