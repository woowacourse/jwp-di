package nextstep.di.factory;

public interface BeanDefinition {
    Class<?> getBeanClass();

    boolean sameBeanClass(Class<?> beanClass);

    Object instantiate(Object... parameterBeans);

    Class<?>[] getParameterTypes();
}
