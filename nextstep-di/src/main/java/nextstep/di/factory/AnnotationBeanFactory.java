package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.bean.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class AnnotationBeanFactory implements BeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, BeanDefinition> beanDefinitionMap;

    public AnnotationBeanFactory(Set<BeanDefinition> beanDefinitions) {
        this.beanDefinitionMap = beanDefinitions.stream()
                .collect(toMap(BeanDefinition::getClazz, identity()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    @Override
    public void initialize() {
        for (Class<?> clazz : beanDefinitionMap.keySet()) {
            beans.putIfAbsent(clazz, instantiateBean(clazz));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiateBean(Class<T> clazz) {
        return (T) beans.getOrDefault(clazz, instantiate(clazz));
    }

    @SuppressWarnings("unchecked")
    private <T> T instantiate(Class<T> clazz) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(clazz);
        Object[] parameter = instantiateParameters(beanDefinition);

        return (T) beanDefinition.instantiate(parameter);
    }

    private Object[] instantiateParameters(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParameterTypes())
                .map(type -> BeanFactoryUtils.findConcreteClass(type, beanDefinitionMap.keySet()))
                .map(this::instantiateBean)
                .toArray(Object[]::new);
    }

    @Override
    public Map<Class<?>, Object> getBeans(Class<? extends Annotation> annotation) {
        return beans.keySet()
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toMap(identity(), key -> beans.get(key)));
    }
}
