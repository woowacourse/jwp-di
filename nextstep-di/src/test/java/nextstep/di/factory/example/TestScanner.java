package nextstep.di.factory.example;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.di.scanner.Scanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class TestScanner implements Scanner {
    private static final Logger log = LoggerFactory.getLogger(TestScanner.class);

    private Reflections reflections;
    private BeanFactory beanFactory;

    public TestScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getAnnotatedClasses() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    public void doScan(String basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.setPreInstanticateBeans(getAnnotatedClasses());
        beanFactory.initialize();
    }
}
