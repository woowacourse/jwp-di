package nextstep.di.factory;

import nextstep.di.factory.instantiation.ConstructorInstantiation;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationBeanScanner {

    private final Reflections reflection;

    public AnnotationBeanScanner(Object... basePackages) {
        this.reflection = new Reflections(basePackages);
    }

    public BeanCreateMatcher scanBean(BeanCreateMatcher beanCreateMatcher, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(annotation);
            typesAnnotatedWith.forEach(clazz -> beanCreateMatcher.put(clazz, new ConstructorInstantiation(clazz)));
        }

        return beanCreateMatcher;
    }
}
