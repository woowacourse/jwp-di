package nextstep.di.factory;

import nextstep.annotation.ComponentScan;
import nextstep.exception.ComponentScanFindFailException;

import java.util.HashSet;
import java.util.Set;

public class ApplicationContext {
    private final Class<?> configureClass;

    public ApplicationContext(final Class<?> configureClass) {
        this.configureClass = configureClass;
    }

    public Set<Class<?>> initialize() {
        String[] basePackage = findBasePackage();
        BeanScanner beanScanner = new BeanScanner(basePackage);
        Set<Class<?>> preInstantiateBeans = beanScanner.scan();
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackage);
        Set<Class<?>> preInstantiateConfigurationBeans = configurationBeanScanner.scan();
        return unify(preInstantiateBeans, preInstantiateConfigurationBeans);
    }

    private Set<Class<?>> unify(Set<Class<?>> preInstantiateBeans, Set<Class<?>> preInstantiateConfigurationBeans) {
        Set<Class<?>> preInstantiateClazz = new HashSet<>();
        preInstantiateClazz.addAll(preInstantiateBeans);
        preInstantiateClazz.addAll(preInstantiateConfigurationBeans);
        return preInstantiateClazz;
    }

    private String[] findBasePackage() {
        if(configureClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = configureClass.getAnnotation(ComponentScan.class);
            return componentScan.basePackages();
        }
        throw new ComponentScanFindFailException();
    }
}
