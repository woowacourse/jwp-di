package nextstep.di;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationScanner {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationScanner.class);

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] CONFIG_ANNOTATIONS = new Class[]{Configuration.class};
    private Set<Class<?>> configClasses;

    public ConfigurationScanner() {
        Reflections reflections = new Reflections("");

        Set<Class<?>> configurations = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : CONFIG_ANNOTATIONS) {
            configurations.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Configuration Type : {}", configurations);
        this.configClasses = configurations;
    }

    public List<String> findPackagesInComponentScan() {
        List<String> basePackages = new ArrayList<>();
        configClasses.forEach(clazz -> appendBasePackagesFromComponentScan(clazz, basePackages));

        return basePackages;
    }

    private void appendBasePackagesFromComponentScan(final Class<?> clazz, final List<String> basePackages) {
        ComponentScan annotation = clazz.getAnnotation(ComponentScan.class);
        basePackages.addAll(Arrays.asList(annotation.basePackages()));
        basePackages.addAll(Arrays.asList(annotation.value()));
    }

    public List<Method> findMethodsWithAnnotation(Class<? extends Annotation> annotation) {
        List<Method> methodsWithAnnotation = new ArrayList<>();
        configClasses.forEach(configClass -> {
            List<Method> methods = appendMethodHavingAnnotation(configClass, annotation);
            methodsWithAnnotation.addAll(methods);
        });

        return methodsWithAnnotation;
    }

    private List<Method> appendMethodHavingAnnotation(final Class<?> configClass, final Class<? extends Annotation> annotation) {
        Method[] declaredMethods = configClass.getDeclaredMethods();

        return Arrays.stream(declaredMethods)
                .filter(declaredMethod -> declaredMethod.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
