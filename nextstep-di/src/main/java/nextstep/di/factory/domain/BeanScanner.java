package nextstep.di.factory.domain;

import com.google.common.collect.Sets;
import nextstep.di.factory.support.Beans;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class BeanScanner {
    private static final Logger logger = LoggerFactory.getLogger(BeanScanner.class);
    private static final Collection<Class<? extends Annotation>> annotations =
            Arrays.asList(Controller.class, Service.class, Repository.class);

    private Beans beans;
    private Reflections reflections;
    private Set<Class<?>> preInstantiateBeans;

    public BeanScanner(Object... basePackage) {
        reflections = new Reflections(basePackage);
        beans = new Beans();
        preInstantiateBeans = scan();
    }

    public void initialize() {
        for (Class clazz : preInstantiateBeans) {
            instantiateBean(clazz);
        }
    }

    private Object instantiateBean(Class<?> clazz) {
        beans.put(clazz, () -> createInstance(clazz));
        return beans.get(clazz);
    }

    private Object createInstance(Class<?> clazz) {
        logger.debug("after getInstance()...");
        logger.debug("Class Type : {}", clazz);

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (injectedConstructor == null) {
            return createBean(clazz);
        }

        Object[] instances = createParameters(injectedConstructor.getParameterTypes());
        return createBeanWithParameters(injectedConstructor, instances);
    }

    private Object[] createParameters(Class<?>[] parameterTypes) {
        Object[] instances = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = BeanFactoryUtils.findConcreteClass(parameterTypes[i], preInstantiateBeans);
            instances[i] = instantiateBean(parameterType);
        }
        return instances;
    }

    private Object createBean(Class<?> clazz) {
        return ReflectionUtils.newInstance(clazz);
    }

    private Object createBeanWithParameters(Constructor<?> injectedConstructor, Object[] instances) {
        return ReflectionUtils.newInstance(injectedConstructor, instances);
    }

    public Set<Class<?>> scan() {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        logger.debug("Scan Beans Type : {}", beans);
        return beans;
    }

    public void scanBeanFactory(BeanFactory2 beanFactory) {
        beans.putAll(beanFactory);
    }
}
