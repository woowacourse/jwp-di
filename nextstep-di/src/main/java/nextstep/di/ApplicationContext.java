package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.di.scanner.Scanner2;

import java.util.HashSet;
import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... clazz) {
        for (Class<?> aClass : clazz) {
            ComponentScan annotation = clazz.getClass().getAnnotation(ComponentScan.class);
            Scanner2 classPathBeanScanner = new ClassPathBeanScanner();
            Set<BeanDefinition> classPathBeanDefinitions = new HashSet<>();

            Scanner2 configurationBeanScanner = new ConfigurationBeanScanner();
            Set<BeanDefinition> configurationBeanDefinitions = new HashSet<>();

            for (String basePackage : annotation.basePackages()) {
                classPathBeanDefinitions.addAll(classPathBeanScanner.scan(basePackage));
                configurationBeanDefinitions.addAll(configurationBeanScanner.scan(basePackage));
            }

            // TODO stream 으로 리팩토링
            classPathBeanDefinitions.addAll(configurationBeanDefinitions);
            this.beanFactory = new BeanFactory(classPathBeanDefinitions);
        }
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }
}
