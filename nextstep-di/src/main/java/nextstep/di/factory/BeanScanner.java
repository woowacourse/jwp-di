package nextstep.di.factory;

import com.google.common.collect.Lists;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> configurations = getTypesAnnotatedWith(reflections, Configuration.class);
        Set<Class<?>> scannedComponents = scanPackagesOfComponentScan(configurations);
        scannedComponents.addAll(configurations);

        return scannedComponents;
    }

    private static Set<Class<?>> scanPackagesOfComponentScan(Set<Class<?>> configurations) {
        Set<Class<?>> scanResult = Sets.newHashSet();

        configurations.stream()
                .map(BeanScanner::scanOfTargetPackages)
                .forEach(scanResult::addAll);

        return scanResult;
    }

    private static Set<Class<?>> scanOfTargetPackages(Class<?> configuration) {
        List<String> targetPackages = getTargetPackages(configuration);

        return scanComponents(targetPackages);
    }

    private static List<String> getTargetPackages(Class<?> configuration) {
        ComponentScan componentScan = configuration.getAnnotation(ComponentScan.class);

        if (componentScan == null) {
            return Lists.newArrayList();
        }

        String[] targetPackages = componentScan.value();
        return targetPackages.length == 0
                ? Arrays.asList(configuration.getPackage().getName())
                : Arrays.asList(targetPackages);
    }

    private static Set<Class<?>> scanComponents(List<String> targetPackages) {
        Set<Class<?>> scannedComponents = new HashSet<>();

        targetPackages.stream()
                .map(BeanScanner::scanPackage)
                .forEach(scannedComponents::addAll);

        return scannedComponents;
    }

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> scanPackage(String basePackage) {
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
