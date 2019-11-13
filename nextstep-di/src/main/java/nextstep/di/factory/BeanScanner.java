package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Component;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    private Reflections reflections;
    private Set<Class<? extends Annotation>> annotations;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
        annotations = new HashSet<>(
                Arrays.asList(Component.class, Controller.class, Repository.class, Service.class));
    }

    public void addAnnotationToScan(Class<? extends Annotation>... annotations) {
        this.annotations.addAll(Arrays.asList(annotations));
    }

    public Set<Class<?>> scanBeans() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
