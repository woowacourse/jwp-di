package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ClassPathScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import nextstep.stereotype.BeanAnnotations;

import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class... configClass) {
        this.beanFactory = new BeanFactory();

        ClassPathScanner classPathScanner = new ClassPathScanner(configClass);
        Set<String> packages = classPathScanner.getPackages();

        registerClassPathBeanDefinition(packages);
        registerConfigurationBeanDefinition(packages);

        beanFactory.initialize();
    }

    private void registerClassPathBeanDefinition(Set<String> packages) {
        ClassPathBeanScanner classPathBeanScanner = new ClassPathBeanScanner(packages);
        classPathBeanScanner.register(beanFactory, BeanAnnotations.getClazz());
    }

    private void registerConfigurationBeanDefinition(Set<String> packages) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(packages);
        configurationBeanScanner.register(beanFactory);
    }

    public Set<Class<?>> getController() {
        return beanFactory.getController();
    }

    public Object getBean(Class<?> clazz) {
        return beanFactory.getBean(clazz);
    }
}
