package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);

    private BeanFactory beanFactory;
    private Reflections reflections;
    private Set<Class<?>> preInstanticateClazz;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doScan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        initiate();
        getPreInstanticateClazz();
    }

    public void getPreInstanticateClazz() {
        beanFactory.addPreInstanticateClazz(preInstanticateClazz);
    }

    @SuppressWarnings("unchecked")
    private void initiate() {
        this.preInstanticateClazz = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
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
