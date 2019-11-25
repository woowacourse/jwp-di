package nextstep.di.factory;

public abstract class BeanDefinition {
    private Class<?> beanClass;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public boolean sameBeanClass(Class<?> beanClass) {
        return this.beanClass.equals(beanClass);
    }
}
