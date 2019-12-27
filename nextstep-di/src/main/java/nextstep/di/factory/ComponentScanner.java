package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {
    private static final Logger log = LoggerFactory.getLogger(ComponentScanner.class);
    private final Reflections reflections;
    private Class<ComponentScan> componentScanClass = ComponentScan.class;

    public ComponentScanner(String root) {
        reflections = new Reflections(root);
    }

    public String[] findBasePackages() {
        Set<Class<?>> typesWithComponentScan = reflections.getTypesAnnotatedWith(componentScanClass);
        Set<String> basePackages = new HashSet<>();

        for (Class<?> type : typesWithComponentScan) {
            basePackages.addAll(Arrays.stream(type.getAnnotation(componentScanClass)
                    .basePackages()).collect(Collectors.toSet()));
        }

        return basePackages.toArray(String[]::new);
    }
}
