package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;
import java.util.stream.Stream;

public class ConfigurationBeanScanner implements BeanScanner {

    private static final Class<Configuration> COMPONENT_ANNOTATION = Configuration.class;

    private final Object[] basePackages;
    private final Set<Class<?>> classTypes;

    public ConfigurationBeanScanner(Class<?>... configurations) {
        this.basePackages = initBasePackages(configurations);
        if (ArrayUtils.isEmpty(basePackages)) {
            throw new IllegalStateException("ComponentScan이 없습니다.");
        }
        this.classTypes = getTypesAnnotatedWith(basePackages, COMPONENT_ANNOTATION);
    }

    private Object[] initBasePackages(Class<?>... configurations) {
        return Stream.of(configurations)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    public Object[] getBasePackages() {
        return basePackages;
    }

    @Override
    public Set<Class<?>> getClassTypes() {
        return classTypes;
    }
}
