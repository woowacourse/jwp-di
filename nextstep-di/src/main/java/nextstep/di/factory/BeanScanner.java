package nextstep.di.factory;

import nextstep.di.factory.scanner.ClassPathBeanScanner;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;

public class BeanScanner {
    private final Object[] basePackage;

    public BeanScanner(Object... basePackage) {
        this.basePackage = BeanScannerUtils.findBasePackage(basePackage);
    }

    public void scanBean(BeanCreateMatcher beanCreateMatcher, Class<? extends Annotation>... annotation) {
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(basePackage);
        classPathBeanScanner.doScan(beanCreateMatcher, annotation);

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackage);
        configurationBeanScanner.register(beanCreateMatcher);
    }

}
