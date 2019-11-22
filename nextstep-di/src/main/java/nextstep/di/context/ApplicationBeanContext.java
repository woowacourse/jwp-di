package nextstep.di.context;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.SingleBeanFactory;
import nextstep.di.registry.BeanRegistry;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.ConfigurationScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationBeanContext implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationBeanContext.class);

    private BeanRegistry beanRegistry = new BeanRegistry();
    private Object[] basePackages;

    public ApplicationBeanContext(Object... basePackages) {
        this.basePackages = initializeBasePackages(basePackages);
        initialize();
    }

    private Object[] initializeBasePackages(Object... basePackages) {
        List<Object> packages = new ArrayList<>();
        packages.addAll(initializePackageName(basePackages));
        packages.addAll(initializeComponentScan(basePackages));

        return packages.toArray();
    }

    private List<Object> initializePackageName(Object... basePackages) {
        return Sets.newHashSet(basePackages).stream()
                .filter(basePackage -> basePackage instanceof String)
                .collect(Collectors.toList());
    }

    private List<String> initializeComponentScan(Object... basePackages) {
        return Sets.newHashSet(basePackages).stream()
                .filter(configuration -> configuration instanceof Class<?>)
                .map(configuration -> (Class<?>) configuration)
                .filter(configuration -> Objects.nonNull(configuration.getAnnotation(ComponentScan.class)))
                .flatMap(configuration -> Arrays.stream(configuration.getAnnotation(ComponentScan.class).value()))
                .collect(Collectors.toList());
    }

    @Override
    public void initialize() {
        BeanFactory beanFactory = new SingleBeanFactory(
                beanRegistry,
                new ConfigurationScanner(basePackages),
                new AnnotatedBeanScanner(basePackages)
        );

        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanRegistry.get(requiredType);
    }

    @Override
    public Set<Class<?>> getTypes(Class<? extends Annotation> annotation) {
        return Collections.unmodifiableSet(beanRegistry.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet()));
    }

    public Object[] getBasePackages() {
        return basePackages;
    }
}
