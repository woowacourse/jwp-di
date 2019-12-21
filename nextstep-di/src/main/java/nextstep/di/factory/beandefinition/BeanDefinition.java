package nextstep.di.factory.beandefinition;

public interface BeanDefinition {

    Class<?> getBeanClass();

    Class<?>[] getParameterTypes();

    Object instantiate(Object[] parameters);
}
