package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.stream.Stream;

public class ComponentScanner {

    private final Object[] basePackages;

    public ComponentScanner(final Class<?>... configurations) {
        this.basePackages = initBasePackages(configurations);
        if (ArrayUtils.isEmpty(basePackages)) {
            throw new IllegalStateException("ComponentScan이 없습니다.");
        }
    }

    private Object[] initBasePackages(Class<?>... configurations) {
        return Stream.of(configurations)
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
                .flatMap(Arrays::stream)
                .toArray();
    }

    public Object[] getBasePackages() {
        return basePackages;
    }
}
