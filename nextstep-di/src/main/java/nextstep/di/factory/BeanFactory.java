package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<BeanDefinition> beanDefinitions = Sets.newHashSet();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if (!beans.containsKey(requiredType)) {
            registerBean(requiredType);
        }
        return (T) beans.get(findConcreteClass(requiredType));
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition);
        }
    }

    private void registerBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return;
        }
        Class<?> beanClass = beanDefinition.getBeanClass();
        beans.put(beanClass, beanDefinition.instantiate(this));
    }

    private void registerBean(Class<?> preInstanticateBean) {
        BeanDefinition beanDefinition = findBeanDefinition(preInstanticateBean);

        registerBean(beanDefinition);
    }

    private BeanDefinition findBeanDefinition(Class<?> concreteClass) {
        if (concreteClass.isInterface() && notExistBeanDefinition(concreteClass)) {
            return findBeanDefinition(findConcreteClass(concreteClass));
        }

        return this.beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.getBeanClass().equals(concreteClass))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean notExistBeanDefinition(Class<?> concreteClass) {
        return this.beanDefinitions.stream()
                .noneMatch(beanDefinition -> beanDefinition.getBeanClass().equals(concreteClass));
    }

    private Class<?> findConcreteClass(Class<?> preInstanticateBean) {
        if (beans.containsKey(preInstanticateBean)) {
            return preInstanticateBean;
        }
        Set<Class<?>> beanClazz = this.beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        return BeanFactoryUtils.findConcreteClass(preInstanticateBean, beanClazz);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Object::getClass, bean -> bean));
    }

    public void register(Set<BeanDefinition> beanDefinitions) {
        this.beanDefinitions.addAll(beanDefinitions);
    }
}
