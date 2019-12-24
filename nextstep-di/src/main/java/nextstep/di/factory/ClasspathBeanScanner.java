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

public class ClasspathBeanScanner {
    private Set<Class<? extends Annotation>> annotations = Sets.newHashSet();

    {
        annotations.add(Controller.class);
        annotations.add(Service.class);
        annotations.add(Repository.class);
    }

    public Map<Class<?>, BeanDefinition> doScan(final Object... basePackage) {
        return Maps.asMap(scan(basePackage), ClasspathBeanDefinition::new);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> scan(final Object... basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }
}
