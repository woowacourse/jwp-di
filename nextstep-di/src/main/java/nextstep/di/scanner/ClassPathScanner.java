package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ClassPathScanner {
    private static final Class<ComponentScan> ANNOTATION_SCAN_CLASS = ComponentScan.class;

    private Set<String> packages;

    public ClassPathScanner(Class<?>... configClasses) {
        this.packages = Stream.of(configClasses)
                .filter(clazz -> clazz.isAnnotationPresent(ANNOTATION_SCAN_CLASS))
                .map(clazz -> clazz.getAnnotation(ANNOTATION_SCAN_CLASS))
                .flatMap(annotation -> Stream.of(annotation.basePackages()))
                .collect(toSet());
    }

    public Set<String> getPackages() {
        return Collections.unmodifiableSet(packages);
    }
}
