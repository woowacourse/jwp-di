package nextstep.di.factory;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner {
    private Class<?>[] classes;

    public ConfigurationBeanScanner(Class<?>... classes) {
        this.classes = classes;
    }

    @Override
    public List<BeanDefinition> doScan() {
        return Arrays.stream(classes)
                .filter(clazz -> clazz.isAnnotationPresent(Configuration.class))
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(this::createMethodBeanDefinition)
                .collect(Collectors.toList());
    }

    private MethodBeanDefinition createMethodBeanDefinition(Method method) {
        try {
            return new MethodBeanDefinition(method.getDeclaringClass().newInstance(), method.getReturnType(), method);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
