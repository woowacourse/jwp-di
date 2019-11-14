package nextstep.di.context;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.BeanDefinitionRegistry;
import nextstep.di.bean.DefaultBeanDefinitionRegistry;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.DefaultBeanFactory;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ComponentScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ApplicationContext implements BeanFactory, BeanDefinitionRegistry {
    private final BeanFactory beanFactory;
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public ApplicationContext(Class<?>... configurations) {
        ComponentScanner componentScanner = new ComponentScanner(configurations);
        Object[] basePackages = componentScanner.getBasePackages();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(basePackages);

        this.beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        beanDefinitionRegistry.register(configurationBeanScanner.getBeanDefinitions());
        beanDefinitionRegistry.register(classpathBeanScanner.getBeanDefinitions());

        this.beanFactory = new DefaultBeanFactory(beanDefinitionRegistry);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beanFactory.findMethodsByAnnotation(methodAnnotation, classAnnotation);
    }

    @Override
    public void register(final BeanDefinition beanDefinition) {
        beanDefinitionRegistry.register(beanDefinition);
    }

    @Override
    public void register(final List<BeanDefinition> beanDefinitions) {
        beanDefinitionRegistry.register(beanDefinitions);
    }

    @Override
    public Collection<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionRegistry.getBeanDefinitions();
    }

    @Override
    public BeanDefinition get(final Class<?> classType) {
        return beanDefinitionRegistry.get(classType );
    }
}
