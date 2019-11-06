package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationScanner;

import java.util.Arrays;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(Class<?> root) {
        ComponentScan scan = root.getAnnotation(ComponentScan.class);
        Object[] basePackages = scan.basePackages();
        if (basePackages.length == 0) {
            basePackages = new Object[]{root.getPackage().getName()};
        }
        beanFactory = new BeanFactory(Arrays.asList(
                new ConfigurationScanner(basePackages),
                new ComponentScanner(basePackages)));
        beanFactory.initialize();
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
