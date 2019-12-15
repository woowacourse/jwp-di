package nextstep;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

public class ComponentScanProcessor {
    private final String packageName;

    public ComponentScanProcessor(final String packageName) {
        this.packageName = packageName;
    }

    public Object[] getBasePackages() {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Configuration.class);

        return typesAnnotatedWith.stream()
                .filter(aClass -> aClass.isAnnotationPresent(ComponentScan.class))
                .map(aClass -> aClass.getAnnotation(ComponentScan.class).basePackages())
                .flatMap(Arrays::stream)
                .toArray();
    }
}
