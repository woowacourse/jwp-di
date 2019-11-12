package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.bean.ConfigurationBeanDefinition;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner implements Scanner {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Configuration.class};

    private Reflections reflections;
    private Object[] basePackages;

    public ConfigurationBeanScanner(Class<?>... configurations) {
        this.basePackages = Arrays.stream(configurations)
                .map(clazz -> clazz.getAnnotation(ComponentScan.class))
                .map(ComponentScan::basePackages)
                .toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<BeanDefinition> scan() {
        reflections = new Reflections(basePackages);
        Set<Class<?>> targets = getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS);
        Set<BeanDefinition> beans = new HashSet<>();
        for (Class<?> target : targets) {
            Set<Method> methods = ReflectionUtils.getMethods(target, ReflectionUtils.withAnnotation(Bean.class));
            for (Method method : methods) {
                beans.add(new ConfigurationBeanDefinition(target, method));
            }
        }
        return beans;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}