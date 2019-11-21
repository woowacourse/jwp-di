package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.bean.BeanDefinition;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitions = new HashMap<>();

    public DefaultBeanFactory(Collection<BeanDefinition> beanDefinitions) {
        initBeanDefinitions(beanDefinitions);
        initBeans();
    }

    private void initBeanDefinitions(Collection<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(beanDefinition -> this.beanDefinitions.put(beanDefinition.getBeanClass(), beanDefinition));
    }

    private Object createBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return beans.get(beanDefinition.getBeanClass());
        }
        logger.debug("create class : {}", beanDefinition.getBeanClass());

        Object[] parameters = createParameters(beanDefinition);
        Object bean = beanDefinition.createBean(parameters);

        beans.put(beanDefinition.getBeanClass(), bean);
        return bean;
    }

    private Object[] createParameters(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(parameter -> BeanFactoryUtils.findConcreteClassByBeanDefinition(parameter, beanDefinitions.values()))
                .map(clazz -> getOrCreateBean(beanDefinitions.get(clazz)))
                .toArray();
    }

    private Object getOrCreateBean(BeanDefinition beanDefinition) {
        return beans.getOrDefault(beanDefinition.getBeanClass(), createBean(beanDefinition));
    }

    private void initBeans() {
        beanDefinitions.values().forEach(this::createBean);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(requiredType, beans.keySet());
        return (T) beans.get(concreteClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(classAnnotation))
                .map(clazz -> ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(methodAnnotation)))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
