package nextstep.di.factory.domain;

import nextstep.di.factory.domain.beandefinition.BeanDefinition;
import nextstep.di.factory.support.Beans;
import nextstep.di.factory.util.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

public class GenericBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(GenericBeanFactory.class);

    private Map<Class<?>, BeanDefinition> beanDefinitions;
    private Set<Class<?>> preInstantiateBeans;
    private Beans beans;

    public GenericBeanFactory() {
        this.beans = new Beans();
        beanDefinitions = new HashMap<>();
        preInstantiateBeans = new HashSet<>();
    }

    @Override
    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            logger.debug("{} construct...", clazz);
            BeanDefinition beanDefinition = beanDefinitions.get(clazz);
            beans.put(clazz, () -> createInstance(beanDefinition));
        }
    }

    private Object createInstance(BeanDefinition beanDefinition) {
        Class<?> beanType = beanDefinition.getBeanType();
        if (beans.contains(beanType)) {
            return beans.get(beanType);
        }

        if (beanDefinition.hasParameter()) {
            return beanDefinition.makeInstance(createParameter(beanDefinition));
        }

        return beanDefinition.makeInstance();
    }

    private Object[] createParameter(BeanDefinition beanDefinition) {
        List<Class<?>> parameters = beanDefinition.getParameters();
        Object[] objects = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            objects[i] = createInstance(
                    beanDefinitions.get(getConcreteClass(parameters.get(i))));
        }
        return objects;
    }

    private Class<?> getConcreteClass(Class<?> clazz) {
        try {
            return BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        } catch (IllegalStateException e) {
            return clazz;
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return beans.getSupportedClass(annotation);
    }

    @Override
    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
    }

    @Override
    public void addInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans.addAll(preInstantiateBeans);
    }
}
