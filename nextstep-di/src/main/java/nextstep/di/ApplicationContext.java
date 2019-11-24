package nextstep.di;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.ClassPathBeanScanner;
import nextstep.di.factory.ConfigurationBeanScanner;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ApplicationContext {
    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    private BeanFactory beanFactory;
    private ConfigurationBeanScanner configurationBeanScanner;
    private ClassPathBeanScanner classPathBeanScanner;

    private ApplicationContext() {
        beanFactory = new BeanFactory();
        configurationBeanScanner = new ConfigurationBeanScanner(beanFactory);
        classPathBeanScanner = new ClassPathBeanScanner(beanFactory);
    }

    public ApplicationContext(Class<?> configurationClass) {
        this();

        initialize(Sets.newHashSet(configurationClass));
    }

    public ApplicationContext(Object... basePackage) {
        this();

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> configurationClasses = reflections.getTypesAnnotatedWith(Configuration.class);

        initialize(configurationClasses);
    }

    private void initialize(Set<Class<?>> configurationClasses) {
        registerComponentScan(configurationClasses);
        configurationBeanScanner.register(configurationClasses);

        beanFactory.initialize();

        log.debug("ApplicationContext Initialized!");
    }

    private void registerComponentScan(Set<Class<?>> classes) {
        classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(ComponentScan.class))
                .map(clazz -> clazz.getAnnotation(ComponentScan.class).basePackages())
                .forEach(classPathBeanScanner::doScan);
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return beanFactory.getAnnotatedClasses(annotation);
    }
}
