package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(BeanScanner.class);

    @SuppressWarnings("unchecked")
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private static Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
