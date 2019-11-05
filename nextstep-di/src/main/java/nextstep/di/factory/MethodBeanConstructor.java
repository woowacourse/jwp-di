package nextstep.di.factory;

import java.lang.reflect.Method;

public class MethodBeanConstructor implements BeanConstructor {

    private final Method constructor;
    private final Object instance;

    public MethodBeanConstructor(Method constructor, Object instance) {
        this.constructor = constructor;
        this.instance = instance;
    }

    @Override
    public Class[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Class<?> getReturnType() {
        return constructor.getReturnType();
    }

    @Override
    public Object construct(Object... parameters) throws Exception {
        return constructor.invoke(instance, parameters);
    }
}
