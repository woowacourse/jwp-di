package nextstep.di.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private List<BeanDefinition> beanDefinitions = Lists.newArrayList();

    private List<Class<?>> beanClasses = Lists.newArrayList();

    public BeanFactory(BeanScanner... beanScanners) {
        for (BeanScanner beanScanner : beanScanners) {
            beanDefinitions.addAll(beanScanner.doScan());
        }
        beanClasses.addAll(getBeanClasses());
        beanDefinitions.forEach(this::registerBean);
    }

    private List<Class<?>> getBeanClasses() {
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toList());
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
        Class<?> concreteClass = findConcreteClass(preInstanticateBean);
        BeanDefinition beanDefinition = findBeanDefinition(concreteClass);

        registerBean(beanDefinition);
    }

    private Class<?> findConcreteClass(Class<?> preInstanticateBean) {
        return BeanFactoryUtils.findConcreteClass(preInstanticateBean, beanClasses);
    }

    private BeanDefinition findBeanDefinition(Class<?> concreteClass) {
        return beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.sameBeanClass(concreteClass))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Object::getClass, bean -> bean));
    }
}
