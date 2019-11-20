package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.exception.NotFoundBeanDefinitionException;
import nextstep.exception.wrapper.ExceptionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Set<BeanDefinition> beanDefinitions = new HashSet<>();

    public BeanFactory() {
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    public void registerBeanDefinitions(Set<BeanDefinition> beanDefinition) {
        beanDefinitions.addAll(beanDefinition);
    }

    public void initialize() {
        beanDefinitions.forEach(bean -> logger.debug("{}", bean));

        beanDefinitions.forEach(ExceptionWrapper.consumerWrapper(this::instantiateBean));
    }

    private void instantiateBean(BeanDefinition beanDefinition) throws Exception {
        List<Object> parameterInstances = new ArrayList<>();
        for (Class<?> param : beanDefinition.getParams()) {
            Class<?> collectClass = BeanFactoryUtils.findCollectClass(param, beanDefinitions);
            if (!beans.containsKey(collectClass)) {
                BeanDefinition paramDefinition = getBeanDefinition(collectClass);
                instantiateBean(paramDefinition);
            }

            parameterInstances.add(beans.get(collectClass));
        }

        logger.debug("create bean : {}", beanDefinition.getName());
        beans.put(beanDefinition.getName(), beanDefinition.createBean(parameterInstances.toArray()));
    }

    private BeanDefinition getBeanDefinition(Class<?> clazz) {
        return beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.matchClass(clazz))
                .findFirst()
                .orElseThrow(NotFoundBeanDefinitionException::new);
    }
}
