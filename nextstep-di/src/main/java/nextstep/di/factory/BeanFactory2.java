package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.bean.BeanDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanFactory2 {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory2.class);

    private Map<Class<?>, BeanDefinition> preBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory2(Set<BeanDefinition> beanDefinitions) {
        this.preBeans = beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinition::getClazz, Function.identity()));
        this.preBeans.forEach(this::initBean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    private void initBean(Class<?> clazz, BeanDefinition beanDefinition) {
        Object[] params = getParams(beanDefinition);
        beans.putIfAbsent(clazz, beanDefinition.invoke(params));
    }

    private Object[] getParams(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParametersType())
                .map(this::getParam)
                .toArray();
    }

    private Object getParam(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }
        initBean(clazz, findBean(clazz));
        return beans.get(clazz);
    }

    private BeanDefinition findBean(Class<?> clazz) {
        if (preBeans.containsKey(clazz)) {
            return preBeans.get(clazz);
        }
        Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preBeans.keySet());
        return preBeans.get(concreteClass);
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return this.beans.entrySet().stream()
                .filter(bean -> bean.getKey().isAnnotationPresent(type))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

