package nextstep.di.factory.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.ComponentDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ComponentScanner.class);

    private Reflections reflections;

    public ComponentScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<BeanDefinition> scan() {
        Set<Class<?>> preInstantiateComponents = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        return preInstantiateComponents.stream()
            .map(ComponentDefinition::new)
            .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
