package nextstep.di.factory.domain;

import com.google.common.collect.Sets;
import nextstep.di.factory.util.BeanFactoryUtils;
import nextstep.di.factory.util.ReflectionUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AnnotationScanner {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);
    private static final Collection<Class<? extends Annotation>> ANNOTATIONS =
            Arrays.asList(Controller.class, Service.class, Repository.class);

    private Reflections reflections;
    private Set<Class<?>> preInstantiateBeans;
    private BeanFactory beanFactory;

    public AnnotationScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void scan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        preInstantiateBeans = scanAnnotations();
        beanFactory.addInstantiateBeans(preInstantiateBeans);

        Set<Class<?>> instantiateBeans = preInstantiateBeans.stream()
                .filter(this::checkAnnotation)
                .collect(toSet());

        for (Class<?> clazz : instantiateBeans) {
            BeanDefinition beanDefinition = createBeanDefinition(clazz);
            beanFactory.addBeanDefinition(clazz, beanDefinition);
        }
    }

    private BeanDefinition createBeanDefinition(Class<?> clazz) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (constructor == null) {
            constructor = getDefaultConstructor(clazz);
        }

        Class<?>[] parameters = constructor.getParameterTypes();
        return new AnnotationBeanDefinition(clazz, constructor, parameters);
    }

    private Constructor<?> getDefaultConstructor(Class<?> clazz) {
        return ReflectionUtils.getDefaultConstructor(clazz);
    }

    private boolean checkAnnotation(Class<?> clazz) {
        return ANNOTATIONS.stream()
                .anyMatch(clazz::isAnnotationPresent);
    }

    private Set<Class<?>> scanAnnotations() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
