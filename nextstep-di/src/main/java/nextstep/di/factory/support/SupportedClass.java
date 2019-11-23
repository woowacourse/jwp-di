package nextstep.di.factory.support;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SupportedClass {
    private Set<Class<?>> supportedClass;

    public SupportedClass() {
        this.supportedClass = new HashSet<>();
    }

    public Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        return supportedClass.stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(toSet());
    }

    public void addSupportedClass(Class<?> clazz) {
        supportedClass.add(clazz);
    }
}
