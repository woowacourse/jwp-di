package nextstep.di.factory;

import nextstep.annotation.Bean;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {
    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
        beanFactory.configurationInitializer(Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(Collectors.toSet()));
    }
}
