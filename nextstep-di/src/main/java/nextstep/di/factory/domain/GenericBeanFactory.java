package nextstep.di.factory.domain;

import nextstep.di.factory.domain.beandefinition.BeanDefinition;
import nextstep.di.factory.domain.beandefinition.SingleInstanceRegistry;
import nextstep.di.factory.support.Beans;
import nextstep.di.factory.support.SupportedClass;

import java.lang.annotation.Annotation;
import java.util.Set;

public class GenericBeanFactory implements BeanFactory {
    private Beans beans;
    private SingleInstanceRegistry singleInstanceRegistry;
    private SupportedClass supportedClass;

    public GenericBeanFactory() {
        this.beans = new Beans();
        this.singleInstanceRegistry = new SingleInstanceRegistry(beans);
        this.supportedClass = new SupportedClass();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (beans.contains(clazz)) {
            return beans.get(clazz);
        }

        return singleInstanceRegistry.createSingleInstance(clazz);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return supportedClass.getClassByAnnotation(annotation);
    }

    @Override
    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        singleInstanceRegistry.addBeanDefinition(clazz, beanDefinition);
        supportedClass.addSupportedClass(beanDefinition.getBeanType());
    }

    @Override
    public void addInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.singleInstanceRegistry.addPreInstantiateBeans(preInstantiateBeans);
    }
}
