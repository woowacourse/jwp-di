package nextstep.di.factory.domain.beandefinition;

import nextstep.di.factory.util.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class BeanDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(BeanDefinitions.class);

    private Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();
    private Set<Class<?>> preInstantiateBeans = new HashSet<>();

    public void addBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
        beanDefinitions.put(clazz, beanDefinition);
    }

    public <T> Object createInstance(Class<T> clazz) {
        return createInstance(beanDefinitions.get(clazz));
    }

    public Object createInstance(BeanDefinition beanDefinition) {
        Class<?> beanType = beanDefinition.getBeanType();
        logger.debug("create {}...", beanType);

        if (beanDefinition.hasParameter()) {
            return beanDefinition.makeInstance(createParameter(beanDefinition));
        }

        return beanDefinition.makeInstance();
    }

    private Object[] createParameter(BeanDefinition beanDefinition) {
        List<Class<?>> parameters = beanDefinition.getParameters();
        Object[] objects = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Class<?> concreteClass = getConcreteClass(parameters.get(i));
            objects[i] = createInstance(beanDefinitions.get(concreteClass));
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

    public void setPreInstantiateBeans(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans.addAll(preInstantiateBeans);
    }

    public Set<Class<?>> getSupportedClass(Class<? extends Annotation> annotation) {
        return beanDefinitions.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }
}
