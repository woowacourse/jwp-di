package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ConfigurationBeanScanner {
    private BeanFactory beanFactory;

    public ConfigurationBeanScanner(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void register(final Class<?>... clazz) {
        beanFactory.initConfigMethod(scan(clazz));
    }

    private Map<Class<?>, Method> scan(final Class<?>... configClass) {
        return Arrays.stream(configClass)
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .collect(toMap(Method::getReturnType, method -> method));
    }
}
