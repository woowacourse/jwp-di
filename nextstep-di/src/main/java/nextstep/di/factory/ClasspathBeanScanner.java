package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.bean.DefaultBeanDefinition;
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

    private final Class[] beanAnnotations = {Controller.class, Service.class, Repository.class};

    private final Reflections reflections;

    public ClasspathBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getPreInstantiateClazz() {
        return getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    }

    @SafeVarargs
    private final Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
