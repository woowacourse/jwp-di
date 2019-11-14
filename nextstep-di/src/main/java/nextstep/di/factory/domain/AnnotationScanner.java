package nextstep.di.factory.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nextstep.di.factory.util.BeanFactoryUtils;
import nextstep.di.factory.util.ReflectionUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationScanner {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);
    private static final Collection<Class<? extends Annotation>> ANNOTATIONS =
            Arrays.asList(Controller.class, Service.class, Repository.class);

    private Reflections reflections;
    private Set<Class<?>> preInstantiateBeans;
    private BeanFactory beanFactory;

    public AnnotationScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void scan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        preInstantiateBeans = scanClass();

        for (Class<?> clazz : preInstantiateBeans) {
            BeanDefinition beanDefinition = makeConstructorBean(clazz);
            beanFactory.addBeanDefinition(clazz, beanDefinition);
        }
    }

    private ConstructorBean makeConstructorBean(Class<?> clazz) {
        clazz = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (constructor == null) {
            return new ConstructorBean(clazz, ReflectionUtils.getDefaultConstructor(clazz), Collections.emptyList());
        }

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 0) {
            return new ConstructorBean(clazz, constructor, Collections.emptyList());
        }

        List<BeanDefinition> parameters = Stream.of(parameterTypes)
                .map(classType -> makeConstructorBean(classType))
                .collect(Collectors.toList());

        return new ConstructorBean(clazz, constructor, parameters);
    }

    private Set<Class<?>> scanClass() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}
