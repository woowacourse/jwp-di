package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.BeanDefinitionRegistry;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final Map<Class<?>, Object> beans = Maps.newHashMap();
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultBeanFactory(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        initBeans(beanDefinitionRegistry.getBeanDefinitions());
    }

    private void initBeans(final Collection<BeanDefinition> beanDefinitions) {
        beanDefinitions.forEach(this::createBean);

    }

    private Object createBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return beans.get(beanDefinition.getBeanClass());
        }
        logger.debug("create Class = {}", beanDefinition.getBeanClass());

        Object[] parameters = createParameters(beanDefinition);
        Object bean = beanDefinition.createBean(parameters);
        beans.put(beanDefinition.getBeanClass(), bean);
        return bean;
    }

    private Object[] createParameters(final BeanDefinition beanDefinition) {
        return Stream.of(beanDefinition.getParameterTypes())
                .map(this::getBean)
                .toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> requiredType) {
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClassByBeanDefinition(requiredType, beanDefinitionRegistry.getBeanDefinitions());
        return (T) beans.getOrDefault(concreteClass, createBean(beanDefinitionRegistry.get(concreteClass)));
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
