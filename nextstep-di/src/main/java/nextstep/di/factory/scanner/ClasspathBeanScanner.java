package nextstep.di.factory.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ClasspathBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ClasspathBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);

    private final Reflections reflections;
    private final Set<Class<? extends Annotation>> DEFAULT_ANNOTATION =
            Stream.of(Controller.class, Service.class, Repository.class).collect(toSet());

    public ClasspathBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public Set<BeanDefinition> scan() {
        return getTypesAnnotatedWith().stream()
                .map(ClasspathBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> getTypesAnnotatedWith() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : DEFAULT_ANNOTATION) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
