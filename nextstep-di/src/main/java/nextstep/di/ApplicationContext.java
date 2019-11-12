package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.BeanFactory2;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.di.scanner.Scanner2;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private BeanFactory2 beanFactory;

    public ApplicationContext(Class<?>... clazz) {
        ComponentScan annotation = clazz[0].getAnnotation(ComponentScan.class);
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
        this.beanFactory = new BeanFactory2(classPathBeanDefinitions);
    }

    public <T> T getBean(Class<T> type) {
        return beanFactory.getBean(type);
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return beanFactory.getBeansWithType(type);
    }
}
