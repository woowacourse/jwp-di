package nextstep.di.definition;

public interface BeanDefinition {

    Class<?> getBeanClass();

    Class<?>[] getParameterTypes();

    Object instantiate(final Object[] parameters);

}
