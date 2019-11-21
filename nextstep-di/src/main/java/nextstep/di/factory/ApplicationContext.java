package nextstep.di.factory;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.scanner.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext implements BeanFactory{
    private final DefaultBeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ComponentScanner componentScanner = new ComponentScanner(configurations);
        Object[] basePackages = componentScanner.getBasePackages();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(basePackages);
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(basePackages);
        MethodBeanScanner methodBeanScanner = new MethodBeanScanner(configurationBeanScanner.getBeanDefinitions());

        Set<BeanDefinition> beanDefinitions = Stream.of(classpathBeanScanner, configurationBeanScanner, methodBeanScanner)
                .map(BeanScanner::getBeanDefinitions)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.beanFactory = new DefaultBeanFactory(beanDefinitions);
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
