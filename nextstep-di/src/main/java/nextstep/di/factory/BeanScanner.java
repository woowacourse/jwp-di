package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Stream;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> scannedComponents = Sets.newHashSet();
        Set<Class<?>> configurations = getTypesAnnotatedWith(reflections, Configuration.class);

        for (Class<?> configuration : configurations) {
            ComponentScan componentScan = configuration.getAnnotation(ComponentScan.class);

            if (componentScan != null) {
                Stream.of(componentScan.value())
                        .map(BeanScanner::scanAnnotation)
                        .forEach(scannedComponents::addAll)
                ;
            }
        }

        scannedComponents.addAll(configurations);
        return scannedComponents;
    }

    @SuppressWarnings("unchecked")
    private static Set<Class<?>> scanAnnotation(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class, Configuration.class);
    }

    @SuppressWarnings("unchecked")
    private static Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
