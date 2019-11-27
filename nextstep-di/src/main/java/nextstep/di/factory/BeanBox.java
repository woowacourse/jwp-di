package nextstep.di.factory;

public interface BeanBox<T> {
    boolean hasParams();

    T getInvoker();

    Object instantiate();

    Object putParameterizedObject(Class<?> preInstanticateBean, Object[] params);

    int getParameterCount();
}
