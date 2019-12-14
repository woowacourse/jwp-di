package nextstep.di.factory.scanner;


import com.google.common.collect.Sets;
import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.bean.ClassPathBeanDefinition;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassPathBeanScanner implements Scanner {
    private static final Logger log = LoggerFactory.getLogger(ClassPathBeanScanner.class);
    private static final Class[] AVAILABLE_ANNOTATIONS = {Controller.class, Service.class, Repository.class};

    private Reflections reflections;

    public ClassPathBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<BeanDefinition> getBeanDefinitions() {
        return getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS);
    }

    @SuppressWarnings("unchecked")
    private Set<BeanDefinition> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            Set<BeanDefinition> eachAnnotationBeanDefinitions = createBeanDefinitions(annotation);
            eachAnnotationBeanDefinitions.addAll(Collections.unmodifiableSet(eachAnnotationBeanDefinitions));
        }
        log.debug("Scan Beans Type : {}", beanDefinitions);
        return beanDefinitions;
    }

    private Set<BeanDefinition> createBeanDefinitions(Class<? extends Annotation> annotation) {
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(annotation);
        return beanClasses.stream()
                .map(ClassPathBeanDefinition::new)
                .collect(Collectors.toSet());
    }
}
