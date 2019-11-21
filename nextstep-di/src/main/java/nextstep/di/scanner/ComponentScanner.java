package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;

import java.util.Arrays;
import java.util.stream.Stream;

public class ComponentScanner {

    private final Object[] basePackages;

    public ComponentScanner(Class<?>... configurations) {
        this.basePackages = initBasePackages(configurations);
    }

    private Object[] initBasePackages(Class<?>... configurations) {
        return Stream.of(configurations)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .flatMap(Arrays::stream)
                .toArray();
    }

    public Object[] getBasePackages() {
        return basePackages;
    }
}
