package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBeanRecipe implements BeanRecipe {
    private static final Logger log = LoggerFactory.getLogger(MethodBeanRecipe.class);

    private Method method;
    private Object instance;
    private Class<?> beanType;
    private Class<?>[] params;

    public MethodBeanRecipe(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
        this.beanType = method.getReturnType();
        this.params = method.getParameterTypes();
    }

    @Override
    public Object bakeBean(Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new BeanCreateException(e);
        }
    }

    @Override
    public Class<?> getBeanType() {
        return beanType;
    }

    @Override
    public Class<?>[] getBeanParamTypes() {
        return params;
    }
}
