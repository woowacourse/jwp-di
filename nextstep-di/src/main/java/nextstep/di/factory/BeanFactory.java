package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<BeanDefinition> beanDefinitions = Sets.newHashSet();

    public BeanFactory(BeanScanner... beanScanners) {
        for (BeanScanner beanScanner : beanScanners) {
            beanDefinitions.addAll(beanScanner.doScan());
        }

        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition);
        }
    }

    private void registerBean(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();

        if (!beans.containsKey(beanClass)) {
            Object[] parameterBeans = getParameterBeans(beanDefinition);
            beans.put(beanClass, beanDefinition.instantiate(parameterBeans));
        }
    }

    private Object[] getParameterBeans(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(this::getBean)
                .toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if (!beans.containsKey(requiredType)) {
            registerBean(requiredType);
        }
        return (T) beans.get(findConcreteClass(requiredType));
    }

    private void registerBean(Class<?> preInstanticateBean) {
        BeanDefinition beanDefinition = findBeanDefinition(preInstanticateBean);

        registerBean(beanDefinition);
    }

    private BeanDefinition findBeanDefinition(Class<?> concreteClass) {
        if (concreteClass.isInterface() && notExistBeanDefinition(concreteClass)) {
            return findBeanDefinition(findConcreteClass(concreteClass));
        }

        return beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.sameBeanClass(concreteClass))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean notExistBeanDefinition(Class<?> concreteClass) {
        return beanDefinitions.stream()
                .noneMatch(beanDefinition -> beanDefinition.sameBeanClass(concreteClass));
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
}
