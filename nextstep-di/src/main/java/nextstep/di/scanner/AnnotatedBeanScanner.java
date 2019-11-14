package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.context.ApplicationBeanContext;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

public class AnnotatedBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(AnnotatedBeanScanner.class);

    private Reflections reflections;
    private Class[] annotations;
    private Set<Class<?>> beans;

    public AnnotatedBeanScanner(ApplicationBeanContext applicationBeanContext, Class<? extends Annotation>... annotations) {
        this.reflections = new Reflections(applicationBeanContext.getRoot());
        this.annotations = annotations;
    }

    @Override
    public Set<Class<?>> doScan() {
        if (beans != null) {
            return beans;
        }

        beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        beans = Collections.unmodifiableSet(beans);

        return beans;
    }
}
