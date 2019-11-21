package nextstep.di.bean;

public interface BeanDefinition<T> {
    Class<T> getClazz();

    Class<?>[] getParameterTypes();

    T instantiate(Object... parameters);
}
