package nextstep.di.factory.scanner;

public class ClassPathBeanDefinition implements BeanDefinition {
    private Class<?> clazz;

    public ClassPathBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }
}
