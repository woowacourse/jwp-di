package nextstep.di.factory;

public interface BeanConstructor {

    Class[] getParameterTypes();
    Class<?> getReturnType();
    Object construct(Object... parameters) throws Exception;
}
