package nextstep.di;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.di.scanner.Scanner;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(configurations);
        Scanner configurationBeanScanner = new ConfigurationBeanScanner(configurations);

        Set<BeanDefinition> beanDefinitions = Stream.of(classPathBeanScanner.scan(), configurationBeanScanner.scan())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.beanFactory = new BeanFactory(beanDefinitions);
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return beanFactory.getBeansWithType(type);
    }
}
