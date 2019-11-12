package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ClassPathBeanDefinition;
import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanScanner implements Scanner2 {
    private static final Logger log = LoggerFactory.getLogger(ClassPathBeanScanner.class);

    private static final Class[] AVAILABLE_ANNOTATIONS = {Controller.class, Service.class, Repository.class};

    private Reflections reflections;

    @Override
    @SuppressWarnings("unchecked")
    public Set<BeanDefinition> scan(String basePackage) {
        reflections = new Reflections(basePackage);
        Set<BeanDefinition> beans = new HashSet<>();
        Set<Class<?>> annotatedClasses = getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS);
        for (Class<?> target : annotatedClasses) {
            beans.add(new ClassPathBeanDefinition(target));
        }
        return beans;
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
}


