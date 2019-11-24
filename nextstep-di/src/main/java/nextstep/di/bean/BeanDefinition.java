package nextstep.di.bean;

public interface BeanDefinition {
    Class<?> getBeanClass();

    Class<?>[] getParameterTypes();

    Object createBean(Object[] parameters);
}
