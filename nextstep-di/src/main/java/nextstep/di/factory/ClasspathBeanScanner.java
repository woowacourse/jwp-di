package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanScanner {
    private BeanFactory beanFactory;
    private Reflections reflections;
    private Set<Class<? extends Annotation>> annotations = Sets.newHashSet();

    {
        annotations.add(Controller.class);
        annotations.add(Service.class);
        annotations.add(Repository.class);
    }

    public ClasspathBeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public ClasspathBeanScanner(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doScan(final Object... basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.initClazz(scan());
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> scan() {
        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        return beans;
    }
}
