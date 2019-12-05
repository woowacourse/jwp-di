package nextstep.di.context;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.AnnotationBeanFactory;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.BeanDefinitionScanner;
import nextstep.di.scanner.ClassPathScanner;
import nextstep.di.scanner.ConfigurationBeanDefinitionScanner;
import nextstep.di.scanner.ConstructorBeanDefinitionScanner;
import nextstep.stereotype.Controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(Class... configClasses) {
        ClassPathScanner classPathScanner = new ClassPathScanner(configClasses);
        Set<String> packages = classPathScanner.getPackages();

        Set<BeanDefinition> beanDefinitions = getBeanDefinitions(packages);

        this.beanFactory = new AnnotationBeanFactory(beanDefinitions);
        beanFactory.initialize();
    }

    private Set<BeanDefinition> getBeanDefinitions(Set<String> packages) {
        BeanDefinitionScanner configScanner = new ConfigurationBeanDefinitionScanner(packages);
        BeanDefinitionScanner constructorScanner = new ConstructorBeanDefinitionScanner(packages);

        Set<BeanDefinition> beanDefinitions = new HashSet<>(configScanner.scan());
        beanDefinitions.addAll(constructorScanner.scan());
        return beanDefinitions;
    }

    public Map<Class<?>, Object> getControllers() {
        return beanFactory.getBeans(Controller.class);
    }
}
