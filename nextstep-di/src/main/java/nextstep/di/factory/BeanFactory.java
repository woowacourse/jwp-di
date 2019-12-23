package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.scanner.BeanScanner;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanFactory {
    private final Map<Class<?>, BeanDefinition> beanDefinitions;
    private final Map<Class<?>, Object> beans;
    private final CircularChecker circularChecker;

    public BeanFactory() {
        this.beans = Maps.newHashMap();
        beanDefinitions = Maps.newHashMap();
        circularChecker = new CircularChecker();
    }

    public void addScanner(BeanScanner beanScanner) {
        beanDefinitions.putAll(beanScanner.scan()
            .stream()
            .collect(Collectors.toMap(BeanDefinition::getType, beanDefinition -> beanDefinition)));
    }

    public Map<Class<?>, Object> initialize() {
        for (Class<?> targetClass : beanDefinitions.keySet()) {
            beans.put(targetClass, generateOrGetBean(targetClass));
        }
        beanDefinitions.clear();
        return beans;
    }

    private Object generateOrGetBean(Class<?> targetClass) {
        if (beans.containsKey(targetClass)) {
            return beans.get(targetClass);
        }

        return generateBean(targetClass);
    }

    private Object generateBean(Class<?> targetClass) {
        BeanDefinition beanDefinition = beanDefinitions.get(targetClass);
        Object[] params = resolveParams(beanDefinition);
        return beanDefinition.generateBean(params);
    }

    private Object[] resolveParams(BeanDefinition beanDefinition) {
        return Arrays.stream(beanDefinition.getParams())
            .peek(circularChecker::check)
            .map(param -> BeanFactoryUtils.findConcreteClass(param, beanDefinitions.keySet()).orElse(param))
            .map(this::generateOrGetBean)
            .peek(param -> circularChecker.remove())
            .toArray();
    }
}
