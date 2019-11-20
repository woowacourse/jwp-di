package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.FactoryMethodBeanDefinition;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void registerConfigurationClass(Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);

        registerComponentScan(configurationClasses);
        configurationClasses.forEach(this::register);
    }

    private void registerComponentScan(Set<Class<?>> classes) {
        classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .forEach(this::doScan);
    }

    private void doScan(ComponentScan componentScan) {
        ClassPathBeanScanner beanScanner = new ClassPathBeanScanner(beanFactory);
        beanScanner.doScan(componentScan.basePackages());
    }

    public void register(Class<?> clazz) {
        try {
            Object instance = clazz.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            Set<BeanDefinition> beanDefinitions = methodsToBeanDefinitions(methods, instance);

            beanFactory.registerBeanDefinitions(beanDefinitions);
        } catch (Exception e) {
            log.debug("ConfigurationBeanScanner Register Exception", e);
            throw new RuntimeException(e);
        }
    }

    private Set<BeanDefinition> methodsToBeanDefinitions(Method[] methods, Object instance) {
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> toBeanDefinition(instance, method))
                .collect(Collectors.toSet());
    }

    private BeanDefinition toBeanDefinition(Object o, Method method) {
        return new FactoryMethodBeanDefinition(o, method);
    }
}
