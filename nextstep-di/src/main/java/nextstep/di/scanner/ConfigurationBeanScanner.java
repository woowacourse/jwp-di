package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.definition.BeanDefinition;
import nextstep.di.definition.ConfigurationBeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.exception.BeanInstantiationException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ConfigurationBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private Reflections reflections;

    public ConfigurationBeanScanner(Set<String> basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public void register(BeanFactory beanFactory) {
        Set<Class<?>> classesWithConfigAnnotation = reflections.getTypesAnnotatedWith(Configuration.class);
        Set<BeanDefinition> beanDefinitions = classesWithConfigAnnotation.stream()
                .map(this::instantiateBeanDefinition)
                .flatMap(Collection::stream)
                .collect(toSet());

        beanFactory.register(beanDefinitions);
    }

    private Set<BeanDefinition> instantiateBeanDefinition(Class<?> clazz) {
        Object declareClass;
        try {
            declareClass = clazz.newInstance();
        } catch (Exception e) {
            log.debug("Fail to instantiate declareClass {}", e.getMessage());
            throw new BeanInstantiationException();
        }
        Method[] methods = clazz.getMethods();
        return scan(declareClass, methods);
    }

    private Set<BeanDefinition> scan(Object declareClass, Method[] methods) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new ConfigurationBeanDefinition(declareClass, method))
                .collect(toSet());
    }
}
