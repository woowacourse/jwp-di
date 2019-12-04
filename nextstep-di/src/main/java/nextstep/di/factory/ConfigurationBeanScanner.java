package nextstep.di.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class ConfigurationBeanScanner {
    private BeanFactory beanFactory;
    private Reflections reflections;

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Set<Method> scanConfigurationBeans(Object... basePackage) {
        reflections = new Reflections(basePackage);
        return getTypesAnnotatedWith(Configuration.class);
    }

    private Set<Method> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> classes = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            classes.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        Set<Method> methods = Sets.newHashSet();
        for (Class clazz : classes) {
            methods = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(Bean.class));
            beanFactory.appendPreInstantiatedMethodsOfBean(methods);
        }

        return methods;
    }
}
