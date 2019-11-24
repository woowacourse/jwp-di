package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.BeanDefinition;
import nextstep.di.ConstructorBeanDefinition;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(AnnotatedBeanScanner.class);
    private static final Class[] ANNOTATIONS = new Class[]{Controller.class, Service.class, Repository.class};

    private Reflections reflections;

    public AnnotatedBeanScanner(Object... basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    @Override
    public Set<BeanDefinition> doScan() {
        Set<BeanDefinition> beans = Sets.newHashSet();

        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(
                    reflections.getTypesAnnotatedWith(annotation).stream()
                            .map(clazz -> new ConstructorBeanDefinition(clazz, BeanFactoryUtils.getInjectedConstructor(clazz)))
                            .collect(Collectors.toSet())
            );
        }

        return beans;
    }
}
