package nextstep.di.factory.domain;

import nextstep.di.factory.support.Beans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class BeanFactory2 implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory2.class);
    private Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();

    private Beans beans;

    public BeanFactory2() {
        this.beans = new Beans();
    }

    public void addBean(Class<?> clazz, Object instance) {
        beans.put(clazz, () -> instance);
    }

    @Override
    public void initialize() {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            logger.debug("{}를 생성할 때 에러난다", clazz);
            beans.put(clazz, () -> makeInstance(beanDefinitions.get(clazz)));
        }
    }

    private Object makeInstance(BeanDefinition beanDefinition) {
        if (beanDefinition.hasParameter()) {
            Object[] objects = beanDefinition.getParameters()
                    .stream()
                    .map(bean -> createInstance(bean))
                    .toArray();
            return beanDefinition.makeInstance(objects);
        }
        return beanDefinition.makeInstance();
    }

    private Object createInstance(BeanDefinition beanDefinition) {
        beans.put(beanDefinition.getBeanType(), () -> makeInstance(beanDefinition));
        return beans.get(beanDefinition.getBeanType());
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }

    @Override
    public Set<Class<?>> getSupportedClassByAnnotation(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }

    @Override
    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
    }
}
