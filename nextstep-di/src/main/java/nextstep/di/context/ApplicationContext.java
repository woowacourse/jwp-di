package nextstep.di.context;

import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanScanner;
import nextstep.di.scanner.ClassPathBeanScanner;
import nextstep.di.scanner.ClassPathScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.util.Set;

public class ApplicationContext {
    private BeanFactory beanFactory;

    public ApplicationContext(Class... configClass) {
        this.beanFactory = new BeanFactory();

        Set<String> packages = scanPackages(configClass);
        registerBeanDefinition(packages);

        beanFactory.initialize();
    }

    private Set<String> scanPackages(Class[] configClass) {
        ClassPathScanner classPathScanner = new ClassPathScanner(configClass);
        return classPathScanner.getPackages();
    }

    private void registerBeanDefinition(Set<String> packages) {
        registerBeanFactoryToBeanScanner(new ClassPathBeanScanner(packages));
        registerBeanFactoryToBeanScanner(new ConfigurationBeanScanner(packages));
    }

    private void registerBeanFactoryToBeanScanner(BeanScanner beanScanner) {
        beanScanner.register(beanFactory);
    }

    public Set<Class<?>> getController() {
        return beanFactory.getController();
    }

    public Object getBean(Class<?> clazz) {
        return beanFactory.getBean(clazz);
    }
}
