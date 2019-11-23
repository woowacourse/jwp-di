package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanDefinition;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.MethodBeanDefinition;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationScanner extends BeanScanner {

    public ConfigurationScanner(Object... basePackage) {
        super(basePackage);
    }

    @Override
    public Set<BeanDefinition> getBeanDefinitions() {
        return getTypesAnnotatedWith(Configuration.class).stream()
                .flatMap(this::getMethodBeanDefinitions)
                .collect(Collectors.toSet());
    }

    private Stream<MethodBeanDefinition> getMethodBeanDefinitions(Class<?> clazz) {
        Object instance = BeanFactoryUtils.instantiate(clazz);
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new MethodBeanDefinition(method, instance));
    }
}
