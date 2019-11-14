package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(Class<?> root) {
        Object[] basePackages = getBasePackages(root);
        beanFactory = new BeanFactory(Arrays.asList(
                new ConfigurationScanner(basePackages),
                new ComponentScanner(basePackages)));
        beanFactory.initialize();
    }

    private Object[] getBasePackages(Class<?> root) {
        ComponentScan scan = root.getAnnotation(ComponentScan.class);
        Object[] basePackages = scan.basePackages();

        return basePackages.length == 0 ? new Object[]{root.getPackage().getName()} : basePackages;
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Set<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return beanFactory.getBeansWithAnnotation(annotation);
    }
}
