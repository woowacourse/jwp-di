package nextstep.di.factory.scanner;

import nextstep.di.factory.BeanCreateMatcher;
import nextstep.di.factory.instantiation.ConstructorInstantiation;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassPathBeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathBeanScanner.class);

    private final Reflections reflection;

    public ClassPathBeanScanner(Object... basePackage) {
        this.reflection = new Reflections(basePackage);
    }

    public void doScan(BeanCreateMatcher beanCreateMatcher, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(annotation);
            typesAnnotatedWith.forEach(clazz -> beanCreateMatcher.put(clazz, new ConstructorInstantiation(clazz)));
        }
        logger.debug("Beans : {}", beanCreateMatcher);
    }
}
