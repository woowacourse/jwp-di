package nextstep.di.factory.domain;

import nextstep.di.factory.domain.beandefinition.BeanDefinition;
import nextstep.di.factory.domain.beandefinition.SingleInstanceRegistry;
import nextstep.di.factory.support.Beans;

import java.util.Set;

public class GenericBeanFactory implements BeanFactory {
    private Beans beans;
    private SingleInstanceRegistry singleInstanceRegistry;

    public GenericBeanFactory() {
        this.beans = new Beans();
        this.singleInstanceRegistry = new SingleInstanceRegistry(beans);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (beans.contains(clazz)) {
            return beans.get(clazz);
        }

        return singleInstanceRegistry.createSingleInstance(clazz);
    }

    @Override
    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        singleInstanceRegistry.addBeanDefinition(clazz, beanDefinition);
    }

    @Override
    public void addInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.singleInstanceRegistry.addPreInstantiateBeans(preInstantiateBeans);
    }
}
