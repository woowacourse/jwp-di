package nextstep.di.factory.domain;

import nextstep.di.factory.domain.beandefinition.BeanDefinition;
import nextstep.di.factory.domain.beandefinition.BeanDefinitions;
import nextstep.di.factory.support.Beans;

import java.lang.annotation.Annotation;
import java.util.Set;

public class GenericBeanFactory implements BeanFactory {
    private BeanDefinitions beanDefinitions;
    private Beans beans;

    public GenericBeanFactory() {
        this.beans = new Beans();
        this.beanDefinitions = new BeanDefinitions(beans);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (beans.contains(clazz)) {
            return beans.get(clazz);
        }

        return (T) beanDefinitions.createSingleInstance(clazz);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return beanDefinitions.getSupportedClass(annotation);
    }

    @Override
    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.addBeanDefinition(clazz, beanDefinition);
    }

    @Override
    public void addInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.beanDefinitions.setPreInstantiateBeans(preInstantiateBeans);
    }
}
