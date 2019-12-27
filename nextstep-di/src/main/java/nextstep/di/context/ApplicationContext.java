package nextstep.di.context;

import nextstep.di.beans.factory.BeanFactory;
import nextstep.di.beans.scanner.AnnotatedTypeBeanScanner;
import nextstep.di.beans.scanner.BeanScanner;
import nextstep.di.beans.scanner.ConfigurationBeanScanner;
import nextstep.di.beans.specification.BeanSpecification;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {
    private final BeanFactory beanFactory;

    public ApplicationContext(Object... basePackages) {
        this.beanFactory = new BeanFactory(getBeanSpecifications(basePackages));
        this.beanFactory.initialize();
    }

    private Set<BeanSpecification> getBeanSpecifications(Object[] basePackages) {
        List<BeanScanner> beanScanners = Arrays.asList(new AnnotatedTypeBeanScanner(basePackages),
                new ConfigurationBeanScanner(basePackages));

        return beanScanners.stream()
                .map(BeanScanner::scan)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Map<Class<?>, Object> getBeansAnnotatedWith(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansAnnotatedWith(annotation);
    }
}
