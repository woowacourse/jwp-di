package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);
    private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(Controller.class, Service.class, Repository.class);

    private Reflections reflections;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public Set<Class<?>> getTypesAnnotated() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
