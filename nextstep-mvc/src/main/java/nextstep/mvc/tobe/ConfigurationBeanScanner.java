package nextstep.mvc.tobe;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.Scanner;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner implements Scanner {
    private Set<Class<?>> configurationClasses = new HashSet<>();

    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> configurationClass) {
        configurationClasses.add(configurationClass);
    }

    public void doConfiguration() {
        beanFactory.setPreInstanticateBeans(configurationClasses);
        beanFactory.initialize();
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses() {
        return configurationClasses;
    }
}
