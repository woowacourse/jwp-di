package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ClassPathBeanDefinition;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanScanner implements Scanner {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Controller.class, Service.class, Repository.class};

    private Reflections reflections;
    private Object[] basePackages;

    public ClassPathBeanScanner(Class<?>... configurations) {
        this.basePackages = Arrays.stream(configurations)
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<BeanDefinition> scan() {
        reflections = new Reflections(basePackages);
        Set<BeanDefinition> beans = new HashSet<>();
        Set<Class<?>> annotatedClasses = getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS);
        for (Class<?> target : annotatedClasses) {
            checkIfInterface(target);
            beans.add(new ClassPathBeanDefinition(target));
        }
        return beans;
    }

    private void checkIfInterface(Class<?> bean) {
        if (bean.isInterface()) {
            throw new InvalidBeanClassTypeException();
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}


