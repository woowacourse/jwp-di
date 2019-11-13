package nextstep.di.factory;

public interface BeanDefinition {
    Class<?> getBeanClass();

    Class<?>[] getParameterTypes();

    Object createBean(Object[] parameters);
}
