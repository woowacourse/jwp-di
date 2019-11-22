package nextstep.di.context;

import com.google.common.base.Strings;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationBeanContext implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationBeanContext.class);

    private BeanRegistry beanRegistry = new BeanRegistry();
    private BeanFactory beanFactory;
    private Object[] basePackages;

    public ApplicationBeanContext(String... basePackageNames) {
        this.basePackages = initializePackageName(basePackageNames);
        initialize();
    }

    public ApplicationBeanContext(Class<?>... configurations) {
        this.basePackages = initializeComponentScan(configurations);
        initialize();
    }

    private Object[] initializePackageName(String... basePackageNames) {
        return Sets.newHashSet(basePackageNames).stream()
                .filter(basePackageName -> !Strings.isNullOrEmpty(basePackageName))
                .collect(Collectors.toList())
                .toArray()
                ;
    }

    private Object[] initializeComponentScan(Class<?>... configurations) {
        return Sets.newHashSet(configurations).stream()
                .filter(configuration -> Objects.nonNull(configuration.getAnnotation(ComponentScan.class)))
                .flatMap(configuration -> Arrays.stream(configuration.getAnnotation(ComponentScan.class).value()))
                .collect(Collectors.toList())
                .toArray()
                ;
    }

    @Override
    public void initialize() {
        beanFactory = new SingleBeanFactory(
                beanRegistry,
                new ConfigurationScanner(basePackages),
                new AnnotatedBeanScanner(basePackages)
        );

        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public Set<Class<?>> getTypes(Class<? extends Annotation> annotation) {
        return beanFactory.getTypes(annotation);
    }
}
