package nextstep.di.factory.beanscanner;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.factory.beancreator.BeanCreator;
import nextstep.di.factory.beancreator.ClassBeanCreator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassBeanScanner implements BeanScanner {

    @Override
    public Map<Class<?>, BeanCreator> scan(Object... basePackages) {
        Set<Class<?>> types = Sets.newHashSet();
        Set<Class<?>> componentScans = getTypesAnnotatedWith(new Reflections(basePackages), ComponentScan.class);
        for (Class<?> clazz : componentScans) {
            String[] basePackage = clazz.getAnnotation(ComponentScan.class).basePackages();
            types.addAll(getTypesAnnotatedWith(new Reflections(basePackage), Controller.class, Service.class, Repository.class));
        }
        return Maps.asMap(types, ClassBeanCreator::new);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Reflections reflections, Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
