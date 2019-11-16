package nextstep.di.factory.beancreator;

import com.google.common.collect.Lists;
import nextstep.di.factory.exception.ObjectInstantiationFailException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MethodBeanCreator implements BeanCreator {
    private final Object instance;
    private final Method method;

    public MethodBeanCreator(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public List<Class<?>> getParams() {
        return Lists.newArrayList(method.getParameterTypes());
    }

    @Override
    public Object instantiate(Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ObjectInstantiationFailException(e);
        }
    }
}