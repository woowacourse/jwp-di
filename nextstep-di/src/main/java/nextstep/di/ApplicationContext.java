package nextstep.di;

import nextstep.annotation.ComponentScan;
import nextstep.di.definition.BeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.util.HashSet;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(final Class<?> entryPoint) {
        final ComponentScan componentScan = entryPoint.getAnnotation(ComponentScan.class);
        Object[] basePackages = componentScan.basePackages();
        if (basePackages.length == 0) {
            basePackages = new Object[]{entryPoint.getPackageName()};
        }
        beanFactory = basePackagesInit(basePackages);
    }

    public ApplicationContext(final Object... basePackages) {
        beanFactory = basePackagesInit(basePackages);
    }

    private BeanFactory basePackagesInit(final Object[] basePackages) {
        final BeanFactory beanFactory;
        final Set<BeanDefinition> beanDefinitions = new HashSet<>();
        beanDefinitions.addAll(new ConfigurationBeanScanner(basePackages).scan());
        beanDefinitions.addAll(new ClasspathBeanScanner(basePackages).scan());
        beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();
        return beanFactory;
    }

    public <T> T getBean(final Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

}
