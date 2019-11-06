package nextstep.di.scanner;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanConstructor;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.MethodBeanConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationScanner extends BeanScanner {

    public ConfigurationScanner(Object... basePackage) {
        super(basePackage);
    }

    @Override
    public Set<BeanConstructor> getBeanConstructors() {
        Map<Class<?>, Object> instances = new HashMap<>();
        return getTypesAnnotatedWith(Configuration.class).stream()
                .peek(cls -> instances.put(cls, BeanFactoryUtils.instantiate(cls)))
                .flatMap(cls -> Arrays.stream(cls.getMethods()).filter(method -> method.isAnnotationPresent(Bean.class)))
                .map(method -> new MethodBeanConstructor(method, instances.get(method.getDeclaringClass())))
                .collect(Collectors.toSet());
    }
}
