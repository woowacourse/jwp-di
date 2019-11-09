package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanConstructor;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.MethodBeanConstructor;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationScanner extends BeanScanner {

    public ConfigurationScanner(Object... basePackage) {
        super(basePackage);
    }

    @Override
    public Set<BeanConstructor> getBeanConstructors() {
        return getTypesAnnotatedWith(Configuration.class).stream()
                .flatMap(this::getMethodBeanConstructors)
                .collect(Collectors.toSet());
    }

    private Stream<MethodBeanConstructor> getMethodBeanConstructors(Class<?> clazz) {
        Object instance = BeanFactoryUtils.instantiate(clazz);
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new MethodBeanConstructor(method, instance));
    }
}
