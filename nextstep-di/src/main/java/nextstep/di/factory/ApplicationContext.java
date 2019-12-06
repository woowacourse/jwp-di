package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.exception.ComponentScanFindFailException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private final Class<?> configureClass;
    private BeanFactory beanFactory;

    public ApplicationContext(final Class<?> configureClass) {
        this.configureClass = configureClass;
    }

    public Set<Class<?>> initialize() {
        String[] basePackage = findBasePackage();

        BeanScanner beanScanner = new BeanScanner(basePackage);
        Set<Class<?>> preInstantiateComponents = beanScanner.scan();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();

        Set<Class<?>> preInstantiateConfigurationBeans = configurationBeanScanner.scan(configureClass);

        this.beanFactory = new BeanFactory(preInstantiateComponents, preInstantiateConfigurationBeans);
        beanFactory.initialize(configureClass);
        return null;
    }

    private String[] findBasePackage() {
        if(configureClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configureClass.getAnnotation(ComponentScan.class);
            return componentScan.basePackages();
        }
        throw new ComponentScanFindFailException();
    }

    private Set<Class<?>> unify(Set<Class<?>> preInstantiateBeans, Set<Class<?>> preInstantiateConfigurationBeans) {
        Set<Class<?>> preInstantiateClazz = new HashSet<>();
        preInstantiateClazz.addAll(preInstantiateBeans);
        preInstantiateClazz.addAll(preInstantiateConfigurationBeans);
        return preInstantiateClazz;
    }

    public <T> T getBean(Class<T> requiredType) {
        return this.beanFactory.getBean(requiredType);
    }
}
