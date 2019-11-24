package nextstep.di.context;

import nextstep.di.bean.BeanDefinitionRegistry;
import nextstep.di.bean.DefaultBeanDefinitionRegistry;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.DefaultBeanFactory;
import nextstep.di.scanner.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Stream;

public class ApplicationContext implements BeanFactory {
    private final DefaultBeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ComponentScanner componentScanner = new ComponentScanner(configurations);
        Object[] basePackages = componentScanner.getBasePackages();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(basePackages);
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);
        MethodBeanScanner methodBeanScanner = new MethodBeanScanner(configurationBeanScanner.getBeanDefinitions());

        BeanDefinitionRegistry beanDefinitionRegistry = registerBeanDefinitionRegistry(classpathBeanScanner, configurationBeanScanner, methodBeanScanner);

        this.beanFactory = new DefaultBeanFactory(beanDefinitionRegistry);
    }

    private BeanDefinitionRegistry registerBeanDefinitionRegistry(BeanScanner... beanScanners) {
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        Stream.of(beanScanners)
                .map(BeanScanner::getBeanDefinitions)
                .forEach(beanDefinitionRegistry::register);
        return beanDefinitionRegistry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beanFactory.findMethodsByAnnotation(methodAnnotation, classAnnotation);
    }
}
