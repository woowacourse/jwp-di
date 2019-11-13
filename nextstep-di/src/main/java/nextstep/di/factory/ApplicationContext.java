package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationContext implements BeanFactory{
    private final DefaultBeanFactory beanFactory;

    public ApplicationContext(Class<?>... configurations) {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configurations);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(configurationBeanScanner.getBasePackages());

        Set<Class<?>> classTypes = Stream.of(configurationBeanScanner.getClassTypes(), classpathBeanScanner.getClassTypes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        this.beanFactory = new DefaultBeanFactory(classTypes);
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
