package nextstep.di.context;

import nextstep.di.AnnotatedTypeBeanScanner;
import nextstep.di.BeanScanner;
import nextstep.di.BeanSpecification;
import nextstep.di.ConfigurationBeanScanner;
import nextstep.di.factory.BeanFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
}