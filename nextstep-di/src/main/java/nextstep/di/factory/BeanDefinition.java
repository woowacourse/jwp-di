package nextstep.di.factory;

public abstract class BeanDefinition {
    private Class<?> beanClass;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public abstract Object instantiate(Object... parameterBeans);

    public abstract Class<?>[] getParameterTypes();
}
