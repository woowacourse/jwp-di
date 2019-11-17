package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.di.scanner.Scanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        Object[] basePackages = extractBasePackages(configurations);
        this.beanFactory = new BeanFactory(getBeanDefinitions(basePackages));
    }

    private Set<BeanDefinition> getBeanDefinitions(Object[] basePackages) {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(basePackages);
        Scanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);

        return Stream.of(classPathBeanScanner, configurationBeanScanner)
                .map(Scanner::scan)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Object[] extractBasePackages(Class<?>[] configurations) {
        return Arrays.stream(configurations)
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return beanFactory.getBeansWithType(type);
    }
}
