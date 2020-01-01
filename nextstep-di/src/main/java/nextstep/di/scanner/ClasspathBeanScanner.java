package nextstep.di.scanner;

import nextstep.di.definition.BeanDefinition;
import nextstep.di.definition.ClasspathBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClasspathBeanScanner implements BeanScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ClasspathBeanScanner.class);
    private static final Set<Class<? extends Annotation>> DEFAULT_ANNOTATIONS =
            Set.of(Controller.class, Service.class, Repository.class);

    private final Reflections reflections;

    public ClasspathBeanScanner(final Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @Override
    public Set<BeanDefinition> scan() {
        return getTypesAnnotatedWith().stream()
                .map(ClasspathBeanDefinition::new)
                .collect(Collectors.toSet());
    }

    private Set<Class<?>> getTypesAnnotatedWith() {
        final Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation : DEFAULT_ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        LOG.debug("Scan Beans Type : {}", beans);
        return beans;
    }

}
