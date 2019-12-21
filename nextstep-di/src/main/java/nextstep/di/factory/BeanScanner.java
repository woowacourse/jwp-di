package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {
    public static Set<Class<?>> scan(Object... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        return getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);
    }

    @SuppressWarnings("unchecked")
    private static Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
