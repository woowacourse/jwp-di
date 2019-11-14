package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class ClassBeanScanner {

    public static Map<Class<?>, BeanCreator> scan(Object... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> types = getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);
        return Maps.asMap(types, ClassBeanCreator::new);
    }

    public Map<Class<?>, BeanCreator> scan2(Object... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> types = getTypesAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);
        return Maps.asMap(types, ClassBeanCreator::new);
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
