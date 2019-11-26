package nextstep.di.factory;

import nextstep.annotation.Bean;

import java.util.Arrays;

public class ConfigurationBeanScanner {

    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .findAny()
                .ifPresent(method -> beanFactory.addPreInvokedMethod(method));
    }
}
