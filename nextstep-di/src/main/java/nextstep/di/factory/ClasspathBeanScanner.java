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

public class ClasspathBeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);

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

    public void doScan(final String basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.initClazz(scan());
        beanFactory.initialize();
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> scan() {
        Set<Class<?>> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);

        return beans;
    }
}
