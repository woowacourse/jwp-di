package nextstep.di.factory.beanscanner;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.annotation.ComponentScan;
import nextstep.di.factory.beancreator.BeanCreator;
import nextstep.di.factory.beancreator.ClassBeanCreator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class ClassBeanScanner implements BeanScanner {
    private static final Class[] COMPONENTS = {Controller.class, Service.class, Repository.class};

    @Override
    public Map<Class<?>, BeanCreator> scan(Object... basePackages) {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> types = Sets.newHashSet();
        types.addAll(getTypesAnnotatedWith(reflections, COMPONENTS));
        types.addAll(getTypesFromComponentScan(reflections));
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

    private Set<Class<?>> getTypesFromComponentScan(Reflections reflections) {
        Set<Class<?>> types = Sets.newHashSet();
        Set<Class<?>> componentScans = getTypesAnnotatedWith(reflections, ComponentScan.class);
        for (Class<?> clazz : componentScans) {
            String[] basePackage = clazz.getAnnotation(ComponentScan.class).basePackages();
            types.addAll(getTypesAnnotatedWith(new Reflections(basePackage), COMPONENTS));
        }
        return types;
    }
}
