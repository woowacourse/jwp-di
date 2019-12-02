package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
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

public class ConfigurationBeanScanner implements BeanScanner {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] CONFIG_ANNOTATIONS = new Class[]{Configuration.class};
    private Reflections reflections;
    private Set<Class<?>> configClasses;

    public ConfigurationBeanScanner() {
        reflections = new Reflections("");
    }

    @Override
    public void scan(Object... basePackage) {
        updateReflectionsWith(basePackage);

        Set<Class<?>> configurations = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : CONFIG_ANNOTATIONS) {
            configurations.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        log.debug("Scan Configuration Type : {}", configurations);
        this.configClasses = configurations;
    }

    private void updateReflectionsWith(final Object... basePackage) {
        if (basePackage.length != 0) {
            reflections = new Reflections(basePackage);
        }
    }

    @Override
    public void registerBeans(BeanFactory beanFactory) {
        List<Method> methodsWithBeanAnnotation = findMethodsWithAnnotation(Bean.class);
        beanFactory.appendPreInstantiateBeanMethods(methodsWithBeanAnnotation);
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
}
