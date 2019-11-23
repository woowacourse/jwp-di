package nextstep.di.domain.scanner;

import com.google.common.collect.Sets;
import nextstep.di.domain.beandefinition.BeanDefinition;
import nextstep.di.domain.factory.BeanFactory;
import nextstep.di.domain.beandefinition.AnnotationBeanDefinition;
import nextstep.di.support.SupportedClass;
import nextstep.di.util.BeanFactoryUtils;
import nextstep.di.util.ReflectionUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class ClassPathScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathScanner.class);
    private static final Collection<Class<? extends Annotation>> ANNOTATIONS =
            Arrays.asList(Controller.class, Service.class, Repository.class);

    private Reflections reflections;

    public ClassPathScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
    }

    public void scan(BeanFactory beanFactory) {
        Set<Class<?>> preInstantiateBeans = scanAnnotations();
        beanFactory.addInstantiateBeans(preInstantiateBeans);

        for (Class<?> clazz : preInstantiateBeans) {
            BeanDefinition beanDefinition = createBeanDefinition(clazz);
            beanFactory.addBeanDefinition(clazz, beanDefinition);
            logger.debug("add {} BeanDefinition", beanDefinition);
        }
    }

    private BeanDefinition createBeanDefinition(Class<?> clazz) {
        Constructor<?> constructor = getDefaultConstructor(clazz);

        Class<?>[] parameters = constructor.getParameterTypes();
        return new AnnotationBeanDefinition(clazz, constructor, parameters);
    }

    private Constructor<?> getDefaultConstructor(Class<?> clazz) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return ReflectionUtils.getDefaultConstructor(clazz);
        }
        return injectedConstructor;
    }

    private Set<Class<?>> scanAnnotations() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : ANNOTATIONS) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    public void scan(SupportedClass supportedClass) {
        Set<Class<?>> preInstantiateBeans = scanAnnotations();

        supportedClass.addSupportedClass(preInstantiateBeans);
    }
}
