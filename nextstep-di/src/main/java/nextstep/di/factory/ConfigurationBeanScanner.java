package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.FactoryMethodBeanDefinition;
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

    public void register(Set<Class<?>> classes) {
        classes.forEach(this::register);
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
