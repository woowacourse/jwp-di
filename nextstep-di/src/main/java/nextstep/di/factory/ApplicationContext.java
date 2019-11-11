package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext implements BeanFactory {
    private BeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configurations);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(configurationBeanScanner.getBasePackages());

        Set<Class<?>> preInstantiatedBeans = Stream.of(configurationBeanScanner.getClassTypes(), classpathBeanScanner.getClassTypes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        beanFactory = new DefaultBeanFactory(preInstantiatedBeans);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public Set<Method> findMethodsByAnnotation(Class<? extends Annotation> methodAnnotation, Class<? extends Annotation> classAnnotation) {
        return beanFactory.findMethodsByAnnotation(methodAnnotation, classAnnotation);
    }
}
