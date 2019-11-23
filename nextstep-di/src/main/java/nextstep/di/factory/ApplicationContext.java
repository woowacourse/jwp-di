package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.scanner.ClasspathBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ConfigurationBeanScanner configScanner = new ConfigurationBeanScanner(configurations);
        ClasspathBeanScanner classpathScanner = new ClasspathBeanScanner(getBasePackages(configurations));
        Map<Class<?>, BeanDefinition> beanDefinitions = scan(Sets.union(configScanner.scan(), classpathScanner.scan()));
        beanFactory = new BeanFactory(beanDefinitions);
    }

    public ApplicationContext(Object... basePackage) {
        ClasspathBeanScanner classpathScanner = new ClasspathBeanScanner(basePackage);
        Map<Class<?>, BeanDefinition> beanDefinitions = scan(classpathScanner.scan());
        beanFactory = new BeanFactory(beanDefinitions);
    }

    public void initialize() {
        beanFactory.initialize();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beanFactory.getBean(requiredType);
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getTypesAnnotatedWith(annotation);
    }

    private Map<Class<?>, BeanDefinition> scan(Set<BeanDefinition> beanDefinitions) {
        return beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinition::getClassType, definition -> definition));
    }

    private Object[] getBasePackages(Class<?>... configurations) {
        return Arrays.stream(configurations)
                .map(config -> config.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }
}
